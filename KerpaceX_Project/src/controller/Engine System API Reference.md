# Engine System API参考
### public class ReactionControlSystem
---
##### public ReactionControlSystem(SpaceCenter.Vessel vessel)
###### Function
&emsp;反应控制系统模块构造函数。
###### Parameter
&emsp;目标飞船对象。

---

##### public void enable()
###### Function
&emsp;使能RCS控制。

---
##### public void disable()
###### Function

&emsp;失能RCS控制。

---
##### public void setEngine(int code, float thrust)
###### Function
&emsp;设置目标编号的引擎的推力。
&emsp;此方法为内部调用的基础方法，非需要自定义动作，不建议使用。

###### Parameter
&emsp;code: 引擎编号。
&emsp;thrust: 引擎推力，0 ~ 1。

---
##### private void setAllEngines()
---
##### private void calculateEngineThrottles()
---
##### public void setForward(float strength)
###### Function
&emsp;设置RCS向前推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向后推进。

---
##### public void setNorth(float strength)
###### Function
&emsp;设置RCS向北（导航球0°）推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向南推进。

---
##### public void setEast(float strength)
###### Function
&emsp;设置RCS向东（导航球90°）推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向西推进。

---
##### public void setNorthRotation(float strength)
###### Function
&emsp;设置RCS以指向北（导航球0°）的角速度推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为以指向南的角速度推进。

---
##### public void setEastRotation(float strength)
###### Function
&emsp;设置RCS以指向东（导航球90°）的角速度推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为以指向西的角速度推进。

---


### public class PropulsionSystem

---
##### public PropulsionSystem(SpaceCenter.Vessel vessel)
###### Function
&emsp;推进系统模块构造函数。
###### Parameter
&emsp;目标飞船对象。

---
##### public void enableMainEngine()
###### Function
&emsp;使能主发动机。

---
##### public void disableMainEngine()
#####Function
&emsp;失能主发动机。

---
##### public void enableAuxiliaryEngines()
###### Function
&emsp;使能辅助发动机。

---
##### public void disableAuxiliaryEngines()
###### Function
&emsp;失能辅助发动机。

---
##### public void enableAllEngines()
###### Function
&emsp;使能所有发动机。

---
##### public void disableAllEngines()
###### Funciton
&emsp;失能所有发动机。

---
##### public void setMainEngineThrottle(float throttle)
###### Function
&emsp;设置主发动机节流阀。
###### Parameter
&emsp;throttle: 节流阀大小，0 ~ 1。

---
##### public void setAuxiliaryEngineThrottle(float throttle)
###### Function
&emsp;设置辅助发动机节流阀。
###### Parameter
&emsp;throttle: 节流阀大小，0 ~ 1。

---
##### public void setAllEngineThrottle(float throttle)
###### Function
&emsp;设置所有发动机节流阀。
###### Parameter
&emsp;throttle: 节流阀大小，0 ~ 1。