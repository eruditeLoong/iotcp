package com.jeecg.position.utils;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class FenceUtil {
    /**
     * 判断点是否在多边形内
     * @param polygon	多边形
     * @param point		检测点
     * @return			点在多边形内返回true，否则返回false
     */
    public static boolean IsPtInPoly(List<Point2D.Double> polygon, Point2D.Double point){
        int N = polygon.size();
        boolean boundOrVertex = true;//如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;//cross points count of x--交叉点计数X
        double precision = 2e-10;//浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;//neighbour bound vertices--临近绑定顶点
        Point2D.Double p = point;//当前点

        p1 = polygon.get(0);//left vertex--左顶点
        for (int i = 1; i <= N; ++i) {//check all rays--检查所有射线
            if(p.equals(p1))
                return boundOrVertex;//p is an vertex--p是一个顶点

            p2 = polygon.get(i % N);//right vertex--右顶点
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests--射线不在我们的兴趣范围之内
                p1 = p2;
                continue;//next ray left point--下一条射线的左边点
            }

            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm(common part of)--射线被算法穿越(常见的一部分)
                if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray--x在射线之前
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray--在一条水平射线上
                        return boundOrVertex;
                    }

                    if(p1.y == p2.y){//ray is vertical--射线是垂直的
                        if(p1.y == p.y){//overlies on a vertical ray--覆盖在垂直光线上
                            return boundOrVertex;
                        }else{//before ray--射线之前
                            ++intersectCount;
                        }

                    }else{//cross point on the left side--左边的交叉点
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y--y的交叉点
                        if(Math.abs(p.y - xinters) < precision){//overlies on a ray--覆盖在射线
                            return boundOrVertex;
                        }

                        if(p.y < xinters){//before ray--射线之前
                            ++intersectCount;
                        }
                    }
                }
            }else{//special case when ray is crossing through the vertex--特殊情况下，当射线穿过顶点
                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2--p交叉p2
                    Point2D.Double p3 = polygon.get((i+1) % N);//next vertex--下一个顶点
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <=Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x--p.x在p1.x和p3.x之间
                        ++intersectCount;
                    }else{
                        intersectCount +=  2;
                    }
                }
            }
            p1 = p2;//next ray left point--下一条射线的左边点
        }
        if(intersectCount % 2 == 0){//偶数在多边形外
            return false;
        }else{//奇数在多边形内
            return true;
        }
    }

    /**
     * 判断点是否在多边形内
     * 步骤：
     * 		①声明一个“画笔”
     * 		②将“画笔”移动到多边形的第一个顶点
     * 		③用“画笔”按顺序将多边形的顶点连接起来
     * 		④用“画笔”将多边形的第一个点连起来，最终形成一个封闭的多边形
     * 		⑤用contains()方法判断点是否在多边形区域内
     * @param polygon	多边形
     * @param point		检测点
     * @return			点在多边形内返回true，否则返回false
     */
    public static boolean contains(List<Point2D.Double> polygon, Point2D.Double point){
        // 多边形顶点小于2个，直接返回true
        if(polygon.size() <= 2){
            return true;
        }
        GeneralPath p = new GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        for(Point2D.Double d : polygon){
            p.lineTo(d.x, d.y);
        }
        p.lineTo(first.x, first.y);
        p.closePath();
        return p.contains(point);
    }

    public static void main(String[] args) {
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();
        Point2D.Double p = new Point2D.Double(-105.23352344383753, 441.9463542094182);
        points.add(p);

        p = new Point2D.Double(-101.07166406192366,490.8843508301667);
        points.add(p);

        p = new Point2D.Double(-46.75248740479849, 489.2087271485574 );
        points.add(p);

        p = new Point2D.Double(-35.465601393488214, 425.2068747961383);
        points.add(p);

        p = new Point2D.Double(-65.21381768823116, 393.5007963623528);
        points.add(p);

        p = new Point2D.Double(-107.44047256799017, 406.0827503073679);
        points.add(p);

        p = new Point2D.Double(-129.66454458108714, 426.31013545908417);
        points.add(p);

        p = new Point2D.Double(-60,428);
        System.out.println(IsPtInPoly(points, p));
        System.out.println(contains(points, p));
    }
}
