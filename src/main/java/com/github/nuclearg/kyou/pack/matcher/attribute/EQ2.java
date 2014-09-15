package com.github.nuclearg.kyou.pack.matcher.attribute;

/**
 * 判断报文节点的属性值是否与期望值相等，是=的同义词
 * 
 * @example [attr=='val']
 *          只匹配存在attr属性，且值为val的报文节点
 * 
 * @author ng
 * 
 */
@OperatorDescription("==")
class EQ2 extends EQ {
}
