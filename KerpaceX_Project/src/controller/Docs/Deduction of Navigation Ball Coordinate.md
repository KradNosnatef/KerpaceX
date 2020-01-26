# 导航球坐标变换推导
目的：将游戏中以竖直向上为极轴、北面为0°的导航球坐标系变换成以飞船前方为极轴、下方为0°的极坐标系，以便编写姿态控制算法<br/>约定$pitch=90°$为$z$轴，$yaw=0°, pitch=0°$为$x$轴，$yaw=270°, pitch=0°$为$y$轴<br/>可得导航球坐标与球坐标变换<br/>
$$
\left\{
	\begin{array}{}
		r=1\\
		\theta=-\frac\pi{180}\alpha_{yaw}\\
		\varphi=\frac\pi{180}\alpha_{pitch}
	\end{array}
\right.\tag{1}
$$

又由球坐标与直角坐标变换得<br/>
$$
\left\{
	\begin{array}{}
		x=cos\left(\frac\pi{180}\alpha_{pitch}\right)cos\left(-\frac\pi{180}\alpha_{yaw}\right)\\
		y=cos\left(\frac\pi{180}\alpha_{pitch}\right)sin\left(-\frac\pi{180}\alpha_{yaw}\right)\\
		z=sin\left(\frac\pi{180}\alpha_{pitch}\right)
	\end{array}
\right.\tag{2}
$$
对于直角坐标系，坐标系绕坐标轴旋转的坐标变换公式为<br/>
$$
R_x\left(\alpha\right)=
\left(
    \begin{matrix}
    1&0&0\\
    0&cos\left(\alpha\right)&sin\left(\alpha\right)\\
    0&-sin\left(\alpha\right)&cos\left(\alpha\right)
    \end{matrix}
\right)\tag{3}
$$
$$
R_y\left(\alpha\right)=
\left(
    \begin{matrix}
    cos\left(\alpha\right)&0&-sin\left(\alpha\right)\\
    0&1&0\\
    sin\left(\alpha\right)&0&cos\left(\alpha\right)
    \end{matrix}
\right)\tag{4}
$$
$$
R_z\left(\alpha\right)=
\left(
    \begin{matrix}
    cos\left(\alpha\right)&sin\left(\alpha\right)&0\\
    -sin\left(\alpha\right)&cos\left(\alpha\right)&0\\
    0&0&1
    \end{matrix}
\right)\tag{5}
$$
下面开始变换<br/>
设飞船此时在原导航球球坐标系坐标为<br/>
$$
^{nav}\boldsymbol p_{vessel}=\left(\begin{array}{c}\alpha_{yaw}\\\alpha_{pitch}\\\alpha_{roll}\end{array}\right)\tag{6}
$$
由 (1) (2) 得<br/>
$$
^{sph}\boldsymbol p_{vessel}=\left(\begin{array}{c}1\\-\frac\pi{180}\alpha_{yaw}\\\frac\pi{180}\alpha_{pitch}\end{array}\right)\tag{7}
$$
$$
^{rec}\boldsymbol p_{vessel}=\left(\begin{array}{c}cos\left(\frac\pi{180}\alpha_{pitch}\right)cos\left(-\frac\pi{180}\alpha_{yaw}\right)\\cos\left(\frac\pi{180}\alpha_{pitch}\right)sin\left(-\frac\pi{180}\alpha_{yaw}\right)\\sin\left(\frac\pi{180}\alpha_{pitch}\right)\end{array}\right)\tag{8}
$$