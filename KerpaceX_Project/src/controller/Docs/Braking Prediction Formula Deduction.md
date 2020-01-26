# 火箭刹车高度与速度关系推导

令	初始质量：$m$；推力：$F$；燃料流量：$Q$；初始速度：$v_0$

由冲量定理
$$
\left(m-Qt\right)dv=\left(F-G\right)dt\tag{1}
$$
整理得
$$
dv=\frac{Fdt}{m-Qt}-gdt\tag{2}
$$
代换得
$$
dv=-\frac FQ\frac{d\left(Qt-m\right)}{Qt-m}-gdt\tag{3}
$$
积分得
$$
v=-\frac FQln\left(m-Qt\right)-gt+C\tag{4}
$$
已知$t=0$时，$v=v_0$，代入 (4) 解得
$$
v=-\frac FQln\left(1-\frac{Qt}m\right)-gt+v_0\tag{5}
$$

即
$$
ds=\left(-\frac FQln\left(1-\frac{Qt}m\right)-gt+v_0\right)dt\tag{6}
$$
上式再积分得
$$
s=\frac {mF}{Q^2}\left(\left(1-\frac{Qt}m\right)ln\left(1-\frac{Qt}m\right)+\frac{Qt}m\right)-\frac g2t^2+v_0t+C\tag{7}
$$
已知$t=0$时，$s=0$，代入 (7) 解得
$$
s=\frac {mF}{Q^2}\left(\left(1-\frac{Qt}m\right)ln\left(1-\frac{Qt}m\right)+\frac {Qt}m\right)-\frac g2t^2+v_0t\tag{8}
$$
由 (5) 式，$v=0$时
$$
-\frac FQln\left(1-\frac{Qt_{v=0}}m\right)-gt_{v=0}+v_0=0\tag{9}
$$
$t_{v=0}$可由$Newton$迭代法求得

此时
$$
s=\frac {mF}{Q^2}\left(\left(1-\frac{Qt_{v=0}}m\right)ln\left(1-\frac{Qt_{v=0}}m\right)+\frac{Qt_{v=0}}m\right)-\frac g2t_{v=0}^2+v_0t_{v=0}\tag{10}
$$