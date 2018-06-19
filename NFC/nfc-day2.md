
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

~~~java
// 1. get NFC Adapter
NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

// 2. Detecting Tag
IntentFilter ndefTagFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
ndefTagFilter.addDataType("text/plain");
// 여러개의 필터 추가 가능.
IntentFilter[] filters = new IntentFilter[]{ndefTagFilter };

// 3. Tag 인식한 후 처리할 Intent 생성
Intent intent = new Intent(this, this.getClass()); // 직접 처리
intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

// 4. onResume에서 등록
nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
~~~

 
 ## [NFC Writer](https://github.com/neoend/mds-android-connectivity/tree/master/NFC/NfcTagWriter)

 p.49 쓰기에 필요한 것은 **Ndef** 객체.
~~~java
// intent 에서 Tag 객체 구해서 Write  
Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
Ndef ndef = Ndef.get(tag);
~~~

  - .connect()
  - .writeNdefMessage(NdefMessage msg) : data가 잘 써졌는지에 대한 검증은 직접 해야함.

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


