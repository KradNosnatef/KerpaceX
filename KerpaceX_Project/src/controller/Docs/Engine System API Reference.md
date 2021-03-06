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
&emsp;设置目标编号的引擎的推力。<br/>
&emsp;此方法为内部调用的基础方法，非需要自定义动作，不建议使用。

###### Parameter
&emsp;code: 引擎编号。<br/>
&emsp;thrust: 引擎推力，0 ~ 1。

---
##### private void setAllEngines()
---
##### private void calculateEngineThrottles()
---
##### private void calulateEngineUnitTorque()
---
##### public void setForward(float strength)
###### Function
&emsp;设置RCS向前推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向后推进。

---
##### public void setUp(float strength)
###### Function
&emsp;设置RCS向上推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向下推进。

---
##### public void setRight(float strength)
###### Function
&emsp;设置RCS向右推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向左推进。

---
##### public void setPitch(float strength)
###### Function
&emsp;设置RCS向俯仰角增大（抬头）方向推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向俯仰角减小方向推进。

---
##### public void setYaw(float strength)
###### Function
&emsp;设置RCS向航向角增大（右转）方向推进的强度。
###### Parameter
&emsp;strength: 推进强度，-1 ~ 1；若为负值，为向航向角减小方向推进。

---
<br/>

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
##### Function
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