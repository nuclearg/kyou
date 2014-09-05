package net.nuclearg.kyou.dom;

import java.util.Map;
import java.util.TreeMap;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.visitor.KyouDomVisitor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 报文元素的基类
 * <p>
 * 整篇报文由报文元素组成，整体上呈现为一棵树
 * <li>报文中的叶子节点称为{@link KyouField}，可以存放具体的值</li>
 * <li>{@link KyouStruct}和{@link KyouArray}分别表示体和数组，这两个类都是继承自{@link KyouContainer}</li>
 * <li>{@link KyouDocument}表示一篇完整的报文</li>
 * <p>
 * "属性"用来描述一个报文元素的细节，例如名称、类型、长度等<br/>
 * 属性的名称和含义可以自由定义<br/>
 * 除了一些预先规定的属性({@link KyouAttributeSpecialNames})外kyou根本不关心这些属性的实际含义，这些完全由组包或拆包时使用的配置文件来定义。
 * </p>
 * 
 * @author ng
 */
public abstract class KyouItem {
    /**
     * 该报文元素的名称
     * <p>
     * “名称”只有作为容器中的一个元素时才有意义，每个元素的名称都由其父节点维护
     * </p>
     */
    protected String name;

    /**
     * 该报文元素的父节点
     */
    protected KyouContainer parent;

    /**
     * 该报文元素的属性列表
     */
    protected Map<String, String> attributes = new TreeMap<String, String>();

    /**
     * 获取该节点的名称
     * <p>
     * 实际上是取name属性的值
     * </p>
     */
    public String name() {
        return this.name;
    }

    /**
     * 获取该报文元素的父元素
     * <p>
     * 注：SchemaDocument的父元素是其自身
     * </p>
     */
    public KyouContainer parent() {
        return this.parent;
    }

    /**
     * 从该节点出发遍历报文树的各子节点并执行指定的操作
     * 
     * @param visitor
     *            在遍历报文树时要执行的操作
     */
    public abstract void foreach(KyouDomVisitor visitor);

    /*
     * ==================== 祖先相关 ====================
     */

    /**
     * 工具方法，一直向祖先追溯
     * 
     * @param handler
     *            追溯到每个父节点时采取的动作
     */
    protected void traceAncestor(AncestorTraceHandler handler) {
        KyouContainer parent = this.parent;
        while (parent != null && parent != parent.parent()) {
            handler.onAncestor(parent);
            parent = parent.parent();
        }
    }

    /**
     * 只有一个入参的回调函数
     * 
     * @author ng
     * 
     * @param <T>
     *            参数类型
     */
    protected static interface AncestorTraceHandler {
        public void onAncestor(KyouContainer container);
    }

    /**
     * 判断当前元素是不是另一个元素的祖先
     * 
     * @param child
     *            判断是否是当前元素子节点的元素
     * @return 当前元素是不是指定元素的祖先
     */
    public boolean isAncestorOf(KyouItem child) {
        final boolean[] flag = { false };

        final KyouItem _this = this;

        this.traceAncestor(new AncestorTraceHandler() {

            @Override
            public void onAncestor(KyouContainer container) {
                if (container == _this)
                    flag[0] = true;
            }
        });

        return flag[0];
    }

    /**
     * 计算该节点的深度
     * 
     * @return 该节点的深度
     */
    public int depth() {
        final int[] depth = { 0 };

        this.traceAncestor(new AncestorTraceHandler() {

            @Override
            public void onAncestor(KyouContainer container) {
                depth[0]++;
            }
        });

        return depth[0];
    }

    /**
     * 获取元素当前的路径
     */
    public String path() {
        final StringBuilder builder = new StringBuilder(this.name);

        this.traceAncestor(new AncestorTraceHandler() {

            @Override
            public void onAncestor(KyouContainer container) {
                builder.insert(0, ".").insert(0, container.name());
            }
        });

        return builder.toString();
    }

    /*
     * ==================== toString相关 ====================
     */

    @Override
    public abstract String toString();

    /**
     * 拼缩进
     * 
     * @return
     */
    protected String buildToStringPrefix() {
        /*
         * 要达成的效果如下：
         * ***
         * |- ***
         * | |- ***
         * | |- ***
         * |- ***
         */

        int depth = this.depth();
        if (depth == 0)
            return "";
        else
            return StringUtils.repeat("| ", this.depth() - 1) + "|-";
    }

    /*
     * ==================== 深度复制相关 ====================
     */

    /**
     * 深度复制
     */
    public abstract KyouItem deepCopy();

    /**
     * 深度复制，只拷贝结构，不关心数据
     */
    public abstract KyouItem deepCopyStruct();

    /*
     * ==================== 属性相关 ====================
     */

    /**
     * 获取该报文元素的属性列表
     */
    public Map<String, String> attributes() {
        return this.attributes;
    }

    /**
     * 获取该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @return 属性的值
     */
    public String attr(String name) {
        return this.attributes.get(name);
    }

    /**
     * 获取该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param defaultValue
     *            取不到时使用的默认值
     * @return 属性的值
     */
    public String attr(String name, String defaultValue) {
        String value = this.attr(name);
        if (value == null)
            value = defaultValue;
        return value;
    }

    /**
     * 设置该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param value
     *            属性的值
     * @return 返回this以便于级联setAttr
     */
    public KyouItem setAttr(String name, String value) {
        if (StringUtils.isEmpty(name))
            throw new KyouException("attribute name is empty. path: " + this.path());
        if (value == null)
            throw new KyouException("attribute value is null. name: " + this.name + ", path: " + this.path());

        this.attributes.put(name, value);
        return this;
    }

    /**
     * 获取指定元素的boolean类型属性的值
     * <p>
     * 当属性值为 "true" 时（不区分大小写）返回true，否则返回false
     * 
     * @param name
     *            属性名称
     * @return 属性的值
     */
    public boolean attrb(String name) {
        return "true".equalsIgnoreCase(this.attributes.get(name));
    }

    /**
     * 设置该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param value
     *            属性的值
     * @return 返回this以便于级联setAttr
     */
    public KyouItem setAttr(String name, boolean value) {
        return this.setAttr(name, String.valueOf(value));
    }

    /**
     * 获取指定元素的int类型属性的值
     * 
     * @param name
     *            属性名称
     * @param defaultValue
     *            取不到或报错时使用的默认值
     * @return 属性的值
     */
    public int attri(String name, int defaultValue) {
        return NumberUtils.toInt(this.attributes.get(name), defaultValue);
    }

    /**
     * 设置该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param value
     *            属性的值
     * @return 返回this以便于级联setAttr
     */
    public KyouItem setAttr(String name, int value) {
        return this.setAttr(name, String.valueOf(value));
    }

    /**
     * 获取指定元素的long类型属性的值
     * 
     * @param name
     *            属性名称
     * @param defaultValue
     *            取不到或报错时使用的默认值
     * @return 属性的值
     */
    public long attrl(String name, long defaultValue) {
        return NumberUtils.toLong(this.attributes.get(name), defaultValue);
    }

    /**
     * 设置该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param value
     *            属性的值
     * @return 返回this以便于级联setAttr
     */
    public KyouItem setAttr(String name, long value) {
        return this.setAttr(name, String.valueOf(value));
    }

    /**
     * 获取指定元素的指定枚举类型属性的值
     * 
     * @param name
     *            属性名称
     * @param defaultValue
     *            取不到或报错时使用的默认值
     * @return 属性的值
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T attre(String name, T defaultValue) {
        try {
            return (T) Enum.valueOf(defaultValue.getClass(), this.attributes.get(name));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 设置该报文元素的某个指定的属性的值
     * 
     * @param name
     *            属性名称
     * @param value
     *            属性的值
     * @return 返回this以便于级联setAttr
     */
    public <T extends Enum<T>> KyouItem setAttr(String name, T value) {
        if (value == null)
            throw new KyouException("attribute value is null. path: " + this.path());
        return this.setAttr(name, String.valueOf(value));
    }
}
