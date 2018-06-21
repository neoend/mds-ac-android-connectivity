# Wi-Fi Direct; [Wi-Fi P2P](https://developer.android.com/guide/topics/connectivity/wifip2p)

### WifiP2pManager

1. initialize()
2. discoverPeer() : 장치를 찾으면 broadcast 날아옴.
3. 2번에서 보낸 인텐트 받아서 requestPeers() 하면서 PeerListListener() 등록
4. connect()
