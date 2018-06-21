# Wi-Fi Direct; [Wi-Fi P2P](https://developer.android.com/guide/topics/connectivity/wifip2p)

### WifiP2pManager

WifiDirectDemo 참고. [구글 설명](https://developer.android.com/training/connect-devices-wirelessly/wifi-direct)

[수업에서 사용한 동일한 예제](http://nospblog.blogspot.com/2013/02/android-wi-fi-direct.html)
1. initialize()
2. discoverPeer() : 장치를 찾으면 broadcast 날아옴.
3. 2번에서 보낸 인텐트 받아서 requestPeers() 하면서 PeerListListener() 등록
4. connect()

[예제1](http://codesunsoo.blogspot.com/2015/05/android-wifi-directwifi-peer-2-peer.html)

[안드로이드 와이파이 다이렉트(WI-FI Direct) 프로그래밍](http://hamait.tistory.com/300)

