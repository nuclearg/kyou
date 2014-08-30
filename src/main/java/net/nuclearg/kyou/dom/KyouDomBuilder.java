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
    private KyouDocument doc = new KyouDocument();
    /**
     * 当前构建栈
     */
    private Stack<KyouContainer> stack = new Stack<KyouContainer>();

    public KyouDomBuilder() {
        this.stack.push(this.doc);
    }

    /**
     * 得到当前结果
     * <p>
     * 多次调用此方法得到的将是同一个对象<br/>
     * 在任何时候调用此方法都将会得到一棵完好的报文结构树，不会存在任何内部状态不一致的现象
     * </p>
     * 
     * @return 当前结果
     */
    public KyouDocument result() {
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
    public void field(String name, Map<String, String> attributes, String value) {
        KyouStruct parent = this.ensureParentStruct();

        KyouField field = new KyouField();
        field.clearAndCopyAttributes(attributes);
        field.value(value);

        parent.add(name, field);
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
