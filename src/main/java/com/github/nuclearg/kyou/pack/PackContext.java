package com.github.nuclearg.kyou.pack;

import com.github.nuclearg.kyou.dom.KyouItem;

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
     * 组包样式
     */
    public final KyouPackStyle style;

    /**
     * 组包核心
     */
    public final Packer packer;

    /**
     * 当前正在使用的组包单元
     */
    public final StyleUnit currentStyle;

    PackContext(KyouItem item, KyouPackStyle style, StyleUnit currentStyle, Packer packer) {
        this.item = item;
        this.style = style;
        this.currentStyle = currentStyle;
        this.packer = packer;
    }

}
