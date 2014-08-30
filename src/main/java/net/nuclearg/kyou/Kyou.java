package net.nuclearg.kyou;

import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.serialize.XmlDomSerializer;
import net.nuclearg.kyou.pack.StyleSpecification;

/**
 * kyou的入口类，提供kyou的基本功能
 * <p>
 * 本类是Kyou的入口，可以实现kyou的主要功能：
 * <li>读取/保存{@link KyouDocument}</li>
 * <li>读取{@link StyleSpecification}</li>
 * <li>kyou最根本的存在价值：使用{@link KyouDocument}和{@link StyleSpecification}执行报文组包过程</li>
 * </p>
 * <p>
 * 在load/save系列方法中默认使用{@link XmlDomSerializer}作为序列化/反序列化实现。<br/>
 * 如果期望使用一个不同的序列化策略，请手工调用其它的序列化/反序列化实现。
 * </p>
 * 
 * @author ng
 */
public class Kyou {

}
