package com.github.nuclearg.kyou.dom.visitor;

import com.github.nuclearg.kyou.dom.KyouArray;
import com.github.nuclearg.kyou.dom.KyouDocument;
import com.github.nuclearg.kyou.dom.KyouField;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.dom.KyouStruct;

/**
 * 使用{@link KyouItem}的forEach()方法遍历报文结构树时进行的操作
 * 
 * @author ng
 * 
 */
public interface KyouDomVisitor {
    /**
     * 提供开始对{@link KyouDocument}的遍历时需要执行的操作
     * 
     * @param doc
     *            当前正被遍历的{@link KyouDocument}对象
     */
    public void docStart(KyouDocument doc);

    /**
     * 提供结束对{@link KyouDocument}的遍历时需要执行的操作
     * 
     * @param doc
     *            当前正被遍历的{@link KyouDocument}对象
     */
    public void docEnd(KyouDocument doc);

    /**
     * 提供开始对{@link KyouStruct}的遍历时需要执行的操作
     * 
     * @param stru
     *            当前正被遍历的{@link KyouStruct}对象
     */
    public void struStart(KyouStruct stru);

    /**
     * 提供结束对{@link KyouStruct}的遍历时需要执行的操作
     * 
     * @param stru
     *            当前正被遍历的{@link KyouStruct}对象
     */
    public void struEnd(KyouStruct stru);

    /**
     * 提供开始对{@link KyouArray}的遍历时需要执行的操作
     * 
     * @param stru
     *            当前正被遍历的{@link KyouArray}对象
     */
    public void arrayStart(KyouArray array);

    /**
     * 提供开始对{@link KyouArray}的遍历时需要执行的操作
     * 
     * @param stru
     *            当前正被遍历的{@link KyouArray}对象
     */
    public void arrayEnd(KyouArray array);

    /**
     * 提供遍历{@link KyouField}时要执行的操作
     * 
     * @param field
     *            当前正被遍历的{@link KyouField}对象
     */
    public void field(KyouField field);

}
