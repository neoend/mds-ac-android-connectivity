# [BLE](https://developer.android.com/guide/topics/connectivity/bluetooth-le)

## [BLE GATT](https://www.bluetooth.com/ko-kr/specifications/gatt/generic-attributes-overview)
### [GATT 규격](https://www.bluetooth.com/ko-kr/specifications/gatt)
03.BleControl
1. 연결
2. discoverServices() 무조건 호출해 줘야 함. 호출하지 않으면 BLE로 부터 data 얻어올 수 없음.
3. sensor 활성화; 서비스의 characteristic = configuration p.84 참조
~~~java
BluetoothGattCharacteristic charistic = gatt.getService(UUID_IRT_SERV).getCharacteristic(UUID_IRT_CONF);
// 이 값은 vendor 에서 정하는 규격이다. vendor 마다 달라짐.
byte value = 1;
byte[] val = new byte[1];
val[0] = value;
charistic.setValue(val);
gatt.writeCharacteristic(charistic);
~~~
4. sensor에 대한 noti. 활성화
~~~java
BluetoothGattCharacteristic characteristic = gatt.getService(UUID_IRT_SERV).getCharacteristic(UUID_IRT_DATA);
gatt.setCharacteristicNotification(characteristic, true);
~~~
5. CCCD(Client Characteristic Configuration Descriptor) switch ON? => data characteristic의 descriptor에 1로 
~~~java
// descriptor 에 대한 UUID 00002902-0000-1000-8000-00805f9b34fb 은 표준으로 정해진 값.
BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
if (descriptor != null) {
  descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE); // byte[] 임.
  gatt.writeDescriptor(descriptor);
}
~~~
