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
import json

class senML(object):
    def __init__(self, bn=None, bt=None, bu=None, bv=None, bver=None, n=None, u=None, v=None, vs=None, vb=None, vd=None, s=None, t=None , ut=None):
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
            
        
    def getJSON():
        senml_dict = {}
        if bn is not None:
            senml_dict["bn"] = self.bn
        if bt is not None:
            senml_dict["bt"] = self.bt
        if bu is not None:
            senml_dict["bu"] = self.bu
        if bv is not None:
            senml_dict["bv"] = self.bv
        if bver is not None:
            senml_dict["bver"] = self.bver
        if n is not None:
            senml_dict["n"] = self.n
        if u is not None:
            senml_dict["u"] = self.u
        if v is not None:
            senml_dict["v"] = self.v
        if vs is not None:
            senml_dict["vs"] = self.vs
        if vb is not None:
            senml_dict["vb"] = self.vb
        if vd is not None:
            senml_dict["vd"] = self.vd
        if s is not None:
            senml_dict["s"] = self.s
        if t is not None:
            senml_dict["t"] = self.t
        if ut is not None:
            senml_dict["ut"] = self.ut
        # Convert dictionary to JSON string
        return json.dumps(senml_dict, separators=(',', ':'))

    def __str__(self):
        return (
            "SenML [ "
            + ("bn=" + str(self.bn) + "  " if self.bn is not None else "")
            + ("bt=" + str(self.bt) + "  " if self.bt is not None else "")
            + ("bu=" + str(self.bu) + "  " if self.bu is not None else "")
            + ("bv=" + str(self.bv) + "  " if self.bv is not None else "")
            + ("bver=" + str(self.bver) + "  " if self.bver is not None else "")
            + ("n=" + str(self.n) + "  " if self.n is not None else "")
            + ("u=" + str(self.u) + "  " if self.u is not None else "")
            + ("v=" + str(self.v) + "  " if self.v is not None else "")
            + ("vs=" + str(self.vs) + "  " if self.vs is not None else "")
            + ("vb=" + str(self.vb) + "  " if self.vb is not None else "")
            + ("vd=" + str(self.vd) + "  " if self.vd is not None else "")
            + ("s=" + str(self.s) + "  " if self.s is not None else "")
            + ("t=" + str(self.t) + "  " if self.t is not None else "")
            + ("ut=" + str(self.ut) + "  " if self.ut is not None else "")
            + "]"
        )
