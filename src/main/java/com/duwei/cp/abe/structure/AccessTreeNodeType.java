package com.duwei.cp.abe.structure;

/**
 * @BelongsProject: JPBC-ABE
 * @BelongsPackage: com.duwei.jpbc.cp.abe.structure
 * @Author: duwei
 * @Date: 2022/7/21 16:59
 * @Description: 访问树的节点类型
 */
public interface AccessTreeNodeType {
    /**
     * 内部节点
     */
    byte INNER_NODE = 1;
    /**
     * 叶节点
     */
    byte LEAF_NODE = 2;
}
