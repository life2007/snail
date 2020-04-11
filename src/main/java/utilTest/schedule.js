/**
 使用透传数据方式,尽量少查数据库
 */
var MessageApiUtil = Java.type('com.sengled.cloud.ruleEngine.common.util.MessageApiUtil');
var JdbcTemplate = Java.type('com.sengled.cloud.ruleEngine.service.MyJdbcTemplate');
var Map = Java.type('java.util.HashMap');
var List = Java.type('java.util.ArrayList');
var LogUtil = Java.type('com.sengled.cloud.ruleEngine.common.util.LogUtil');
var DynamoUtil = Java.type('com.sengled.cloud.ruleEngine.common.util.DynamoUtil');


function DeviceScene(brightness,colorTemperature,onoff,gradientTime,deviceUuid){
    this.brightness=brightness;
    this.colorTemperature=colorTemperature;
    this.onoff=onoff;
    this.gradientTime=gradientTime;
    this.deviceUuid=deviceUuid;
}

function ClientMsg2MessageAPI(dn,data,productCode,code,password){
    this.dn = dn;
    this.data = data;
    this.productCode = 'zigbee';
    this.code = 'zigbee';
    this.password = '123456';
}

function ApplySceneData(deviceSceneList){
    this.interCode = 13;
    this.deviceList = deviceSceneList;
}

function DeviceSetGroupData(deviceUuidList, val){
    this.interCode = 16;
    this.value = val;
    this.deviceUuidList = deviceUuidList;
}

function SharedDevice(deviceUuid){
    this.deviceUuid = deviceUuid;
}

function DownMsgVO(firstMsgList,secondMsgList){
    this.firstMsgList = firstMsgList;
    this.secondMsgList = secondMsgList;
}

var zigbeeDataSource = "zigbee";
var lifeDataSource = "life2";

function info(msg){
    LogUtil.info('message.js :'+msg);
}

/**
 * java调用js的入口
 * @param payload    json格式字符串
 */
function execute(payload){
    info('receive msg:'+payload);
    try{
        var msg = JSON.parse( payload );
        buildAndSendMsg(msg);
    }catch (e) {
        // TODO: handle exception
        LogUtil.error('handle msg error,'+e.message);
    }
}

/**
 * 构造消息
 * @param payload
 * {
	type:定时任务类型,
	sceneId:房间的Id,
	alarmId:定时任务Id,
	source:"element/life",
	alarmConfig:[
		{
			"brightness":"xxx",
			"colorTemperature":"xxx"
		}
	],
	devices:{
		element：[{
			gatewayUuid:xxx,
			deviceUuids:[xx,xxx]
			protocolType:"mqtt/work"
		}],
		wifiDevice:[xxx,xxx]
	}
}
 * @returns
 */
function buildAndSendMsg(payloadArr){

    for (var i = 0; i < payloadArr.length; i++) {
        var payload = payloadArr[i];
        try{
            var sceneId = payload.sceneId;
            info(0 + " --"+sceneId );
            //2、要控制的设备,透传
            var deviceInfos =  payload.devices;
            if(!deviceInfos){
                error("invalid payload,"+JSON.stringify(payload));
                return;
            }
            var dnDevices = deviceInfos.element;
            info(1 + " --"+sceneId );
            var wifiElementDevices = deviceInfos.wifiDevice;
            info(2 + " --"+sceneId );
            //3、组装消息下发
            if(dnDevices && dnDevices.length > 0){
                info(3 + " --"+sceneId );
                var deviceMap = dispatchElementDevice(dnDevices);
                info(4 + " --"+sceneId );

                //处理work协议网关控灯
                var elementDeviceList = deviceMap.elementDeviceList;
                if (elementDeviceList && elementDeviceList.length > 0) {
                    info(5 + " --"+sceneId +'---'+JSON.stringify(elementDeviceList));
                    try {
                        integateElementDeviceData(elementDeviceList,  payload);
                    } catch (e) {
                        LogUtil.error('schedule No1 error,' + JSON.stringify(payload) +',' +e.message +','+e.stack);
                    }
                    info(6 + " --"+sceneId );
                }
                //处理mqtt协议网关控灯
                if (deviceMap.element2DeviceList && deviceMap.element2DeviceList.length > 0) {
                    info(7 + " --"+sceneId );
                    integateElement2DeviceData(deviceMap.element2DeviceList, alarmConfig, payload)
                }
                LogUtil.info('buildAndSendMsg 2 sceneId='+sceneId);
            } else {
                LogUtil.warn('no dn device in room['+payload.sceneId+'],stop schedule');
            }
            info(8 + " --"+sceneId );
            if(wifiElementDevices && wifiElementDevices.length > 0) {
                info(9 + " --"+sceneId );
                integateWifiElementData(wifiElementDevices, alarmConfig, payload)
            } else {
                LogUtil.warn('no wifi device in room['+payload.sceneId+'],stop schedule');
            }

        }catch (e) {
            // TODO: handle exception
            LogUtil.error('new invoke payload['+JSON.stringify(payload)+'] error,'+e.message);
            LogUtil.error('invoke payload['+JSON.stringify(payload)+'] error,'+e.stack);
        }
    }
}


function getWifiElementDeviceInfo(alarmId) {
    var sql = "select device_uuid deviceUuid from life2.alarm_device_relation where setting_id = :alarmId and product_code = 'wifielement'";
    var parm = new Map();
    parm.put('alarmId',alarmId);
    var devices = JdbcTemplate.findByParams(sql,parm);
    return devices;
}

/**
 * 整合同一网关下的数据
 * @param devices
 * @param payloadJson
 * @returns
 */
function integateElementDeviceData(devices, payloadJson){
    var type = payloadJson.type;
    var sceneId = payloadJson.sceneId;
    info('integateElementDeviceData 1--'+sceneId);
    switch(type){
        case 3:
        case 4:{
            info('integateElementDeviceData 2--'+sceneId);
            try {
                var downMsg = produceRoomData(devices, payloadJson);
                MessageApiUtil.jsDownMsg(downMsg);
            } catch (e) {
                LogUtil.error('schedule No2 error,'+JSON.stringify(payloadJson) +','+e.message + ',' +e.stack);
            }
            break;
        }
        case 5:{
            var downMsg = produceWakeupData(devices,payloadJson);
            MessageApiUtil.jsWakeupDownMsg(downMsg);
            break;
        }
        case 6:
            var downMsg = produceSmartSocketData(devices,1);
            MessageApiUtil.jsDownMsg(downMsg);
            break;
        case 7: {
            var downMsg = produceSmartSocketData(devices,0);
            MessageApiUtil.jsDownMsg(downMsg);
            break;
        }
        default:{
            LogUtil.error('invalid schedule type['+type+']');
        }
    }
}

/**
 * 整合同一网关下的数据(element2)
 * @param devices
 * @param payloadJson
 * @returns
 */
function integateElement2DeviceData(devices, alarmConfig, payloadJson){
    var type = payloadJson.type;
    handleElement2Device(devices, alarmConfig, type);
}

function integateWifiElementData(devices, alarmConfig, payloadJson){
    var type = payloadJson.type;
    switch(type){
        case 3:
            devices.forEach(function(deviceUuid){
                var topic = 'wifielement/' + deviceUuid + "/update";
                var currentTime = new Date().getTime();
                var downMsg = [{dn:deviceUuid, type:"brightness",value:brightnessValeTransform(alarmConfig.brightness), time:currentTime}];
                MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(downMsg)})
            });
            break;
        case 4:{
            devices.forEach(function(deviceUuid){
                var topic = 'wifielement/' + deviceUuid + "/update";
                var currentTime = new Date().getTime();
                var downMsg = [{dn:deviceUuid, type:"switch",value:"0", time:currentTime}];
                MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(downMsg)})
            });
            break;
        }
        case 5:{
            var typeList = new List();
            typeList.add('switch');

            var tableName = DynamoUtil.DYNAMO_TABLE_DEVICE_STATUS;
            var deviceAttrMap = DynamoUtil.batchGetItemByDnsAndTypesWithMap(tableName,devices,typeList);
            info("wifi deviceAttrs:" + deviceAttrMap);

            devices.forEach(function(deviceUuid){
                var attr = deviceAttrMap[deviceUuid];
                if (attr && attr.switch==0) { //开关状态为0时发起唤醒
                    var currentTime = new Date().getTime();
                    var topic = 'wifielement/' + deviceUuid + "/update";
                    var downMsg =  [{dn:deviceUuid, type:"still",value:{
                            beginBrightness: 0,
                            endBrightness: 50,
                            still: 900
                        }, time:currentTime}]
                    MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(downMsg)})
                } else {
                    LogUtil.warn('the device switch status is not 0, can not warkup');
                }
            });
            break;
        }
        default:{
            LogUtil.error('invalid schedule type['+type+']');
        }
    }
}

/**
 * 将0-255的亮度值转换为0-100
 * @param brightnessVal
 * @returns {string}
 */
function brightnessValeTransform(brightnessVal) {
    var bright = parseInt(brightnessVal);
    if (!isNaN(bright)) {
        bright = bright > 255 ? 255 : bright;
        bright = bright < 0 ? 0 : bright;
        bright = bright * (100/255);
    } else {
        bright = 0;
    }
    var retVal = parseInt(bright)
    return retVal.toString();
}


/**
 * 产生房间的场景及关灯消息
 * @param devices
 * @param payloadJson
 * @returns
 */
function produceRoomData(deviceArray,  payloadJson){
    var alarmConfig = payloadJson.alarmConfig;
    var sceneId = payloadJson.sceneId;
    info('produceRoomData 0 --'+sceneId);
    var bright = alarmConfig.brightness;
    var cct = alarmConfig.colorTemperature;
    var onoff = bright > 0 ? 1:0;
    var gradientTime = 5;
    var map = new Map();
    var type = payloadJson.type;

    info('produceRoomData 1 --'+sceneId);
    deviceArray.forEach(function(element){
        var devices = element.deviceUuids;
        var gatewayUuid = element.gatewayUuid;
        info('produceRoomData 2 --'+sceneId);
        if(devices && devices.length > 0){
            info('produceRoomData 3 --'+sceneId);
            for (var i = 0; i < devices.length; i++) {
                var deviceUuid = devices[i];
                info('produceRoomData 4 --'+sceneId + '---'+ i);
                var deviceScene=buildDeviceScene(bright, cct, onoff, gradientTime, deviceUuid,type);
                info('produceRoomData 5 --'+sceneId + '---'+ i);
                var deviceScenes = map[gatewayUuid];
                if(deviceScenes){

                    deviceScenes.add(deviceScene);
                }else{
                    deviceScenes = new List();
                    deviceScenes.add(deviceScene);
                    map.put(gatewayUuid,deviceScenes);
                }
            }
            info('produceRoomData 6 --'+sceneId);

            // devices.forEach(function(device){
            //     var deviceScene=buildDeviceScene(bright, cct, onoff, gradientTime, device,type);
            //     var deviceScenes = map[gatewayUuid];
            //     if(deviceScenes){
            //         deviceScenes.add(deviceScene);
            //     }else{
            //         deviceScenes = new List();
            //         deviceScenes.add(deviceScene);
            //         map.put(gatewayUuid,deviceScenes);
            //     }
            // })
        }
    });
    info('produceRoomData 7 --'+sceneId);
    var clientMsg2MessageAPIList ;
    if(type==3){
        clientMsg2MessageAPIList =  processSceneMsg(map);
    }else if(type==4){
        clientMsg2MessageAPIList =  processOnOffMsg(map, 0);
    }else{
        LogUtil.error('invalid type['+type+']');
    }
    return clientMsg2MessageAPIList;
}

/**
 * 分发element设备和element2设备
 * @param devices
 * @returns {{elementDeviceList: Array, element2DeviceList: Array}}
 */
function dispatchElementDevice(devices){
    var elementDeviceArr = [];
    var element2DeviceArr = [];
    var gatewayUuidList = new List();
    devices.forEach(function (device) {
        if (device.gatewayUuid) {
            gatewayUuidList.add(device.gatewayUuid);
        }
    })

    var gatewayProductCodeMap = new Map();
    if (gatewayUuidList.size() > 0) {
        var sql = "select uuid gatewayUuid, product_code productCode from zigbee.gateway_info where uuid in (:gatewayUuidList)";
        var parm = new Map();
        parm.put("gatewayUuidList", gatewayUuidList);
        var gatewayProductCodeList = JdbcTemplate.findByParams(sql,parm);
        if (gatewayProductCodeList && gatewayProductCodeList.size() > 0) {
            for(var i=0;i<gatewayProductCodeList.size();i++){
                gatewayProductCodeMap.put(info.gatewayUuid, info.productCode);
            }

            gatewayProductCodeList.forEach(function (info) {
                gatewayProductCodeMap.put(info.gatewayUuid, info.productCode);
            })
        }
    }

    devices.forEach(function(element){
        var gatewayUuid = element.gatewayUuid;
        //此网关为mqtt协议，需要通过mqtt方式控灯
        if (gatewayUuid && gatewayProductCodeMap.get(gatewayUuid) &&
            gatewayProductCodeMap.get(gatewayUuid) == 'element') {
            element2DeviceArr.push(element);
        } else {
            elementDeviceArr.push(element);
        }
    });
    return {elementDeviceList: elementDeviceArr, element2DeviceList: element2DeviceArr};
}

/**
 * 处理element2设备
 * @param element2DeviceList
 * @param roomConfig
 */
function handleElement2Device(element2DeviceList, roomConfig, type) {
    var brightness;
    var colorTemperature;
    if (roomConfig) {
        brightness = roomConfig.brightness;
        colorTemperature = roomConfig.colorTemperature;
    }
    element2DeviceList.forEach(function(element){
        var deviceUuids = element.deviceUuids;
        var gatewayUuid = element.gatewayUuid;

        var topic = "element/" + gatewayUuid + "/update";
        var currentTime = new Date().getTime();

        if (type == 3) {
            var valueArr = [];
            if(deviceUuids && deviceUuids.length > 0){
                deviceUuids.forEach(function(deviceUuid){
                    var valueObj = {
                        deviceUuid: deviceUuid,
                        gradientTime: 10,
                        onoff: 1,
                        brightness: brightness,
                        colorTemperature: colorTemperature
                    }
                    valueArr.push(valueObj);
                })
            }
            var mqttMsg = [{
                dn: gatewayUuid,
                type: "scene",
                time:currentTime,
                value: valueArr
            }]
            MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(mqttMsg)})
        }

        if (type == 4 || type == 6 || type == 7) {
            if (deviceUuids && deviceUuids.length > 0) {
                var downMsg = [{
                    dn: gatewayUuid,
                    type: "groupSwitch",
                    value: {
                        switch: type == 6 ? '1' : '0',
                        gradientTime: 10,
                        deviceUuidList: deviceUuids
                    },
                    time:currentTime
                }]
                MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(downMsg)})
            }
        }

        if (type == 5) {
            var typeList = new List();
            typeList.add('onOff');
            var tableName = DynamoUtil.DYNAMO_TABLE_DEVICE_STATUS;
            var deviceAttrs = DynamoUtil.batchGetItemByDnsAndTypesWithMap(tableName,deviceUuids,typeList);
            info("deviceAttrs:" + deviceAttrs);

            var deviceUuidsForWakeup = [];
            if (deviceUuids && deviceUuids.length > 0) {
                deviceUuids.forEach(function (deviceUuid) {
                    var devicesAttribute = deviceAttrs[deviceUuid];
                    if (devicesAttribute) {
                        var onoff = devicesAttribute.onOff;
                        if (onoff == '0') {
                            deviceUuidsForWakeup.push(deviceUuid);
                        } else {
                            info("element2 wakeup:" + deviceUuid + " onoff=1, need not wakeup");
                        }
                    }
                })
            }
            if (deviceUuidsForWakeup && deviceUuidsForWakeup.length > 0) {
                var downMsg = [{
                    dn: gatewayUuid,
                    type: "wakeup",
                    value: {
                        deviceUuidList: deviceUuids,
                        beginBright: 1,
                        endBright: 128,
                        beginColorTemperature: 1,
                        endColorTemperature: 58,
                        gradientTime: 9000
                    },
                    time:currentTime
                }]
                MessageApiUtil.jsMqttDownMsg({topic: topic, downMsg: JSON.stringify(downMsg)})
            } else {
                info("element2 wakeup: no device need wakeup");
            }
        }
    });
}

/**
 * 产生智能插座的控制信息
 * @param device
 * @param paload
 * @returns {*}
 */
function produceSmartSocketData(deviceList, onoff) {
    var map = new Map();
    var deviceSceneList = new List();
    deviceSceneList.add(new SharedDevice(deviceList[0].deviceUuids[0]));
    map.put(deviceList[0].gatewayUuid,deviceSceneList);
    var clientMsg2MessageAPIList = processOnOffMsg(map, onoff);
    return clientMsg2MessageAPIList;
}

/**
 * 产生wakeup 数据
 * @param devices
 * @param payloadJson
 * @returns
 */
function produceWakeupData(devices,payloadJson){
    var type = payloadJson.type;
    //置零指令
    var firstMap = new Map();
    // wakeup变化指令
    var secondMap = new Map();
    var uuids = new Array();
    //聚合所有的灯，然后查询其onoff属性
    devices.forEach(function(element){
        var devices = element.deviceUuids;
        if(devices && devices.length > 0){
            uuids = uuids.concat(devices);
        }
    })
    var typeList = new List();
    typeList.add('onOff');
    var tableName = DynamoUtil.DYNAMO_TABLE_DEVICE_STATUS;
    var deviceAttrs = DynamoUtil.batchGetItemByDnsAndTypesWithMap(tableName,uuids,typeList);
    info("deviceAttrs:" + deviceAttrs);
    devices.forEach(function(element) {
        var gatewayUuid = element.gatewayUuid;
        var deviceUuids = element.deviceUuids;
        deviceUuids.forEach(function (deviceUuid) {
            var devicesAttribute = deviceAttrs[deviceUuid];
            if (devicesAttribute) {
                var onoff = devicesAttribute.onOff;
                if (onoff == '0') {
                    var firsDeviceScene = buildDeviceScene(1, 1, 0, 0, deviceUuid, type);
                    addDeviceScene(firstMap, gatewayUuid, firsDeviceScene);
                    //变化共计15分钟，第二阶段减1秒，单位是100ms
                    var secondDeviceScene = buildDeviceScene(128, 58, 1, 8990, deviceUuid, type);
                    addDeviceScene(secondMap, gatewayUuid, secondDeviceScene);

                }
            }
        })
    })

    var first =  processSceneMsg(firstMap);
    var second =  processSceneMsg(secondMap);
    var downMsgVO = new DownMsgVO(first,second);
    return downMsgVO;
}

function addDeviceScene(map,gatewayUuid,deviceScene){
    var deviceScenes = map[gatewayUuid];
    if(deviceScenes!=null){
        deviceScenes.add(deviceScene);
    }else{
        deviceScenes = new List();
        deviceScenes.add(deviceScene);
        map.put(gatewayUuid,deviceScenes);
    }
}


/**
 * 产生场景下发消息
 * @param map
 * @returns {List}
 */
function processSceneMsg(map){
    var clientMsg2MessageAPIList = new List();
    if(map && map.size() > 0){
        for(var key in map){
            var deviceSceneList = map[key];
            var clientMsg2MessageAPI=buildSceneMsg2MessageAPI(key,deviceSceneList);
            clientMsg2MessageAPIList.add(clientMsg2MessageAPI);
        }
    }
    return clientMsg2MessageAPIList;
}

function processOnOffMsg(map, onoff){
    var clientMsg2MessageAPIList = new List();
    if(map && map.size() > 0){
        for(var key in map){
            var deviceSceneList = map[key];
            var clientMsg2MessageAPI=buildOnOffMsg2MessageAPI(key,deviceSceneList, onoff);
            clientMsg2MessageAPIList.add(clientMsg2MessageAPI);
        }
    }
    return clientMsg2MessageAPIList;
}

/**
 * 构建下发消息
 * @param dn
 * @param deviceSceneList
 * @returns {___anonymous4291_4293}
 */
function buildSceneMsg2MessageAPI(dn,deviceSceneList){
    var applySceneData = new ApplySceneData(deviceSceneList);
    var msg = new ClientMsg2MessageAPI(dn,applySceneData);
    return msg;
}

function buildOnOffMsg2MessageAPI(dn,deviceSceneList, onoff){
    var data = new DeviceSetGroupData(deviceSceneList, onoff);
    var msg = new ClientMsg2MessageAPI(dn,data);
    return msg;
}


/**
 * 构建场景消息
 * @param bright
 * @param cct
 * @param onoff
 * @param gradientTime
 * @param deviceUuid
 * @param type
 * @returns
 */
function buildDeviceScene(bright,cct,onoff,gradientTime,deviceUuid,type){
    switch(type){
        // 房间场景
        case 3:
        case 5:{
            var deviceScene = new DeviceScene(bright,cct,onoff,gradientTime,deviceUuid);
            return deviceScene;
            break;
        }
        //房间关灯
        case 4:{
            var shareDevice = new SharedDevice(deviceUuid);
            return shareDevice;
            break;
        }
        default:{
            LogUtil.error('invalid type['+type+']');
        }
    }
}
