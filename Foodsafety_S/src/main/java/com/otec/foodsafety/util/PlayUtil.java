package com.otec.foodsafety.util;

public class PlayUtil {
    public static String getPlayUrl(String indexCode){
        String url = "hikvideoclient://ReqType:PlayReal;VersionTag:UNIV1.0;ImpIp:218.90.173.69;ImpPort:8087;Camlist:"
                + indexCode
                + ";loginName:tyyy;LoginPwd:f3056b0f234e9fee0032e9d72e002a393ed29ab07baf77050a608be1fb4254abf401b0794157f1a6411874763a7eca8a61485c4480eeffb59d9e7d33eeba6df1f17dc225323e133037bb68360565d0600b26c89d92df445d8fd1461fc0af1303b7ddcd9a8b9330ed847c349892208860f483955548d150509e265dd1477a11777bac27ba56d4e6aee798cc131e38ddd9df570483d1f166c9b24e6ac38a6f9440b3c0abd3d0809fd8286bd7a3a0f5a220c733a3aa826f91911c00649b602e453e341ca8ead059019443d2b3d50085fec884d6599205bc2a8e263e4c1333b9c6ba95c1e4f37dffc0bf394136bb96dfb5f2cebee87a8954c3e1bbe027c138df697d";
        return url;
    }
}
