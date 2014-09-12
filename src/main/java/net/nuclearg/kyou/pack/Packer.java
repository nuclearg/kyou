package net.nuclearg.kyou.pack;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.util.ByteOutputStream;

/**
 * 组包核心类 提供kyou的通用组包服务
 * 
 * @author ng
 */
public class Packer {

    /**
     * 对整篇报文执行组包过程
     * 
     * @param doc
     *            报文数据
     * @param style
     *            组包样式
     * @return 组包好的报文
     */
    public byte[] packDocument(KyouDocument doc, KyouPackStyle style) {
        // 将doc作为一个item直接组包
        PackContext context = new PackContext(doc, style, this);
        ByteOutputStream os = new ByteOutputStream();
        this.packItem(context, os);
        return os.export();
    }

    /**
     * 对某个指定的报文元素执行组包过程
     * 
     * @param context
     *            组包上下文
     */
    public void packItem(PackContext context, ByteOutputStream os) {
        // 选择适合这个报文节点的style
        StyleItem style = selectStyle(context);

        // 处理style中的每一段，将处理结果作为报文体输出
        for (StyleFormatSegment segment : style.segments)
            segment.export(context, os);
    }

    /**
     * 对某个报文元素的子元素执行组包过程
     * <p>
     * <li>当该元素的array为true时会遍历该数组的各个元素</li>
     * <li>否则将假设当前元素为一个结构并遍历该结构的各个子元素</li>
     * </p>
     * 
     * @param container
     *            被组包的父元素
     * @param context
     *            组包上下文
     */
    public byte[] packMember(KyouContainer container, PackContext context) {
        ByteOutputStream os = new ByteOutputStream();

        for (KyouItem item : container)
            this.packItem(new PackContext(item, context.style, this), os);

        return os.export();
    }

    /**
     * 从组包样式中选择出一个与当前的组包上下文相配套的样式单元
     * 
     * @param context
     *            组包上下文
     * @return 适合当前组包上下文的样式单元
     */
    private static StyleItem selectStyle(PackContext context) {
        for (StyleItem style : context.style.styles)
            if (style.matcher.matches(context.item))
                return style;

        throw new KyouException("no style suitable. path: " + context.item.path() + ", item: " + context.item);
    }
}
