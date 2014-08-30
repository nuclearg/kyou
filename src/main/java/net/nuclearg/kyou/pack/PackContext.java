package net.nuclearg.kyou.pack;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 组包上下文 封闭了组包过程中会用到的一些上下文信息
 * 
 * @author ng
 */
public class PackContext {
    /**
     * 当前正被组包的元素
     */
    public final KyouItem item;

    /**
     * 组包样式定义
     */
    public final StyleSpecification spec;

    /**
     * 组包核心
     */
    public final Packer packer;

    PackContext(KyouItem item, StyleSpecification spec, Packer packer) {
        this.item = item;
        this.spec = spec;
        this.packer = packer;
    }

}
