 #+---------------+------+---------+
 #|         SenML | JSON | Type    |
 #+---------------+------+---------+
 #|     Base Name | bn   | String  |
 #|     Base Time | bt   | Number  |
 #|     Base Unit | bu   | String  |
 #|    Base Value | bv   | Number  |
 #|       Version | bver | Number  |
 #|          Name | n    | String  |
 #|          Unit | u    | String  |
 #|         Value | v    | Number  |
 #|  String Value | vs   | String  |
 #| Boolean Value | vb   | Boolean |
 #|    Data Value | vd   | String  |
 #|     Value Sum | s    | Number  |
 #|          Time | t    | Number  |
 #|   Update Time | ut   | Number  |
 #+---------------+------+---------+
 
class senML:
    bn:str;
    bt:float;
    bu:str;
    bv:float;
    bver:int;
    n:str;
    u:str;
    v:int;
    vs:str;
    vb:bool;
    vd:str;
    s:float;
    t:float;
    ut:int;

    def __init__(self, bn=null, bt=null, bu=null, bv=null, bver=null, n=null, u=null, v=null, vs=null, vb=null, vd=null, s=null, t=null , ut=null):
        self.bn = bn
        self.bt = bt
        self.bu = bu
        self.bv = bv
        self.bver = bver
        self.n = n
        self.u = u
        self.v = v
        self.vs = vs
        self.vb = vb
        self.vd = vd
        self.s = s
        self.t = t
        self.ut = ut
        
    def __str__(self):
        return "SenML [ " + ("bn="+bn+"  " if bn != null else "") + ("bt="+bt+"  " if bt != null else "")
                + ("bu=" + bu + "  " if bu != null else "") + ("bv=" + bv + "  " if bv != null "")
                + (bver != null ? "bver=" + bver + "  " : "") + ("n=" + n + "  " if n != null else "")
                + ("u=" + u + "  " if u != null else "") + ("v=" + v + "  " if v != null "")
                + ("vs=" + vs + "  " if vs != null else "") + ("vb=" + vb + "  " if vb != null else "")
                + ("vd=" + vd + "  " if vd != null else "") + ("s=" + s + "  " if s != null else "")
                + ("t=" + t + "  " if t != null else "") + ("ut=" + ut + "  " if ut != null else "") + "]";