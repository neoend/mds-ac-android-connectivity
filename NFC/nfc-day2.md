
## Foreground Tag Dispatch System

일반적으로는 표준 Tag Dispatch System 과 Foreground Tag Dispatch System 둘 다 등록함.

[NfcForegroundReader](https://github.com/neoend/mds-android-connectivity/tree/master/NFC/NfcForegroundReader)

AndroidManifest.xml
~~~xml
<uses-permission android:name="android.permission.NFC" />
~~~

Activity 기본 구조
~~~java
@Override  
protected void onResume() {  
    super.onResume();  
}  

@Override  
protected void onNewIntent(Intent intent) {  
    super.onNewIntent(intent);  
}  

@Override  
protected void onPause() {  
    super.onPause();  
}
~~~

p.47
 - enableForegroundDispatch(activity, intent, filters, techLists)
 - disableForegroundDispatch(activity)
 
 ## [NFC Writer](https://github.com/neoend/mds-android-connectivity/tree/master/NFC/NfcTagWriter)
 p.49 쓰기에 필요한 것은 **Ndef** 객체.
  - .connect()
  - .writeNdefMessage() : data가 잘 써졌는지에 대한 검증은 직접 해야함.

1. 특정 type의 data 를 NdefRecord 생성.
2. NdefRecord로 NdefMessage 생성.

~~~java
private NdefMessage makeNdefMsg(String data, String pkg) {
    // 1. NdefRecord 생성
    NdefRecord uriRec = NdefRecord.createUri(data);
    NdefRecord aarRec = NdefRecord.createApplicationRecord(pkg);
    NdefRecord[] recArr = new NdefRecord[]{uriRec, aarRec};
    // 2. NdefMessage 생성
    NdefMessage ndefMsg = new NdefMessage(recArr);
    return ndefMsg;
}
~~~


