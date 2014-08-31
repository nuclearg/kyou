package net.nuclearg.kyou.dom;

import java.util.Map;
import java.util.Stack;

import net.nuclearg.kyou.KyouException;

/**
 * 工具类 用于根据需求建造出一棵DOM树
 * 
 * @author ng
 */
public class KyouDomBuilder {
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

    public void beginDocument(Map<String, String> attributes) {
        this.doc = new KyouDocument();
        this.doc.clearAndCopyAttributes(attributes);

        this.stack.push(this.doc);
    }

    public KyouDocument endDocument() {
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
    public void beginStruct(String name, Map<String, String> attributes) {
        KyouStruct stru = new KyouStruct();
        stru.clearAndCopyAttributes(attributes);

        KyouStruct parent = this.ensureParentStruct();
        parent.add(name, stru);

        this.stack.push(stru);
    }

    /**
     * 关闭当前最外面的{@link KyouStruct}
     */
    public void endStruct() {
        this.ensureParentStruct();

        this.stack.pop();
    }

    /**
     * 构建一个域
     * 
     * @param attributes
     *            该域元素的属性
     */
    public void beginField(String name, Map<String, String> attributes) {
        KyouStruct parent = this.ensureParentStruct();

        KyouField field = new KyouField();
        field.clearAndCopyAttributes(attributes);

        parent.add(name, field);

        this.currentField = field;
    }

    /**
     * 关闭当前的域
     * 
     * @param value
     */
    public void endField(String value) {
        this.currentField.value(value);
    }

    /**
     * 构建一个{@link KyouArray}
     * 
     * @param name
     *            名称
     * @param attributes
     *            属性列表
     */
    public void beginArray(String name, Map<String, String> attributes) {
        throw new UnsupportedOperationException();
    }

    /**
     * 关闭当前最外面的{@link KyouArray}
     */
    public void endArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * 声明下一个构建的元素是当前数组的原型
     */
    public void arrayPrototypeStart() {
        throw new UnsupportedOperationException();
    }

    public void arrayPrototypeEnd() {
        throw new UnsupportedOperationException();
    }

    public void beginArrayElementStruct() {
        throw new UnsupportedOperationException();
    }

    public void endArrayElementStruct() {
        throw new UnsupportedOperationException();
    }

    public void arrayField(String value) {
        throw new UnsupportedOperationException();
    }

    private KyouStruct ensureParentStruct() {
        KyouContainer top = this.stack.peek();
        if (top instanceof KyouStruct)
            return (KyouStruct) top;
        else
            throw new KyouException("top of stack must struct");
    }
}
