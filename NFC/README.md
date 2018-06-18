


# NFC
다음 2개 파일 설치
 - [NFC TagInfo](https://play.google.com/store/apps/details?id=at.mroland.android.apps.nfctaginfo)
 - [NFC TagWriter by NXP](https://play.google.com/store/apps/details?id=com.nxp.nfc.tagwriter)

**Tag**

코일로 감긴 140 byte 정도의 메모리가 있음.(500번 정도 read/write 가능)
Tagging으로 특정 action 바로 실행 가능.

**P2P**

Google - Android Beam(magic sharing)
 - 큰 사이즈의 데이터는 Bluetooth 사용
 -- Bluetooth 페어링 + 전송
 - Wi-Fi direct 추가하려고 했으나 OEM에서 이미 자체 기능으로 집어 넣음.
 -- LG는 Smart Direct
 
## NFC tag 인식과 intent 처리
※ http://chiyo85.tistory.com/70


Test project 1: **[NfcReader](https://github.com/neoend/mds-android-connectivity/tree/master/NFC/NfcReader)**

~~~xml
<activity android:name=".MainActivity">
  <intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
  </intent-filter>
  <intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:mimeType="text/plain" />
  </intent-filter>
</activity>
~~~
 - 여러 개의 intent-fileter가 들어갈 수 있다.
   - android.nfc.action.NDEF_DISCOVERED
   - android.nfc.action.TECH_DISCOVERED
   - android.nfc.action.TAG_DISCOVERED

