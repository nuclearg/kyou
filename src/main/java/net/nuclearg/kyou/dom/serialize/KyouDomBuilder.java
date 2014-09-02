package net.nuclearg.kyou.dom.serialize;

import java.util.Map;
import java.util.Stack;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouArray;
import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouDocument;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.dom.KyouStruct;

/**
 * 工具类 用于根据需求建造出一棵DOM树
 * 
 * @author ng
 */
class KyouDomBuilder {
    /**
     * 当前正被构造的{@link KyouDocument}对象
     */
    private KyouDocument doc;
    /**
     * 当前构建栈
     */
    private Stack<KyouContainer> stack = new Stack<KyouContainer>();
    /**
     * 当前构建的域
     */
    private KyouField currentField;

    /**
     * 构建一个{@link KyouDocument}
     * 
     * @param attributes
     */
    void beginDocument(Map<String, String> attributes) {
        this.doc = new KyouDocument();
        this.doc.attributes().putAll(attributes);

        this.stack.push(this.doc);
    }

    /**
     * 完成整个{@link KyouDocument}的构建
     * 
     * @return 构建好的整篇报文数据
     */
    KyouDocument endDocument() {
        return this.doc;
    }

    /**
     * 构建一个{@link KyouStruct}
     * 
     * @param name
     *            名称
     * @param attributes
     *            该结构元素的属性
     */
    void beginStruct(String name, Map<String, String> attributes) {
        KyouStruct stru = new KyouStruct();
        stru.attributes().putAll(attributes);

        KyouStruct parent = this.ensureParentStruct();
        parent.add(name, stru);

        this.stack.push(stru);
    }

    /**
     * 关闭当前最外面的{@link KyouStruct}
     */
    void endStruct() {
        this.ensureParentStruct();

        this.stack.pop();
    }

    /**
     * 构建一个{@link KyouField}
     * 
     * @param attributes
     *            该域元素的属性
     */
    void beginField(String name, Map<String, String> attributes) {
        KyouStruct parent = this.ensureParentStruct();

        KyouField field = new KyouField();
        field.attributes().putAll(attributes);

        parent.add(name, field);

        this.currentField = field;
    }

    /**
     * 关闭当前的{@link KyouField}
     * 
     * @param value
     *            元素的值
     */
    void endField(String value) {
        if (this.currentField == null)
            throw new KyouException("current field is not ready");

        this.currentField.value(value);
    }

    /**
     * 构建一个数组
     * 
     * @param name
     *            名称
     * @param attributes
     *            属性列表
     */
    void beginArray(String name, Map<String, String> attributes) {
        throw new UnsupportedOperationException();
    }

    /**
     * 关闭当前最外面的{@link KyouArray}
     */
    void endArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * 声明下一个构建的元素是当前数组的原型
     */
    void beginArrayPrototype() {
        this.ensureParentArray();
        throw new UnsupportedOperationException();
    }

    /**
     * 结束数组原型构建
     */
    void endArrayPrototype() {
        this.ensureParentArray();
        throw new UnsupportedOperationException();
    }

    private KyouStruct ensureParentStruct() {
        if (this.stack.isEmpty())
            throw new KyouException("build stack empty");

        KyouContainer top = this.stack.peek();
        if (top instanceof KyouStruct)
            return (KyouStruct) top;
        else
            throw new KyouException("top of stack must a struct");
    }

    private KyouArray ensureParentArray() {
        if (this.stack.isEmpty())
            throw new KyouException("build stack empty");

        KyouContainer top = this.stack.peek();
        if (top instanceof KyouArray)
            return (KyouArray) top;
        else
            throw new KyouException("top of stack must an array");
    }
}
