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
 
 
