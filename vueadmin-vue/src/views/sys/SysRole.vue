<template>
  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="getRoleList">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="roleDialogVisible=true">新增</el-button>
      </el-form-item>
      <el-form-item>
        <template>
          <el-popconfirm title="确定批量删除选中的数据吗？" @confirm="delRoleHandle">
            <el-button type="danger" slot="reference"
                       :disabled="multipleSelection.length === 0">批量删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-form-item>
    </el-form>

    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          width="55">
      </el-table-column>

      <el-table-column
          prop="name"
          label="名称"
          width="120">
      </el-table-column>
      <el-table-column
          prop="code"
          label="唯一编码"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="remark"
          label="描述"
          show-overflow-tooltip>
      </el-table-column>


      <el-table-column
          prop="status"
          label="状态">
        <template v-slot="scopedProps">
          <el-tag size="small" v-if="scopedProps.row.status === 1" type="success">正常</el-tag>
          <el-tag size="small" v-else-if="scopedProps.row.status === 0" type="danger">禁用</el-tag>
        </template>

      </el-table-column>

      <el-table-column
          prop="created"
          width="200"
          label="创建时间"
      >
      </el-table-column>
      <el-table-column
          prop="icon"
          width="260px"
          label="操作">

        <template slot-scope="scopedProps">

          <el-button type="text" @click="editRowPermHandle(scopedProps.row.id)">分配权限</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-button type="text" @click="editRow(scopedProps.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>

          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="delRoleHandle(scopedProps.row.id)">
            <el-button type="text" slot="reference">删除</el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>

    <!--分页控件-->
    <div class="block">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
          :current-page="pagination.current"
          :page-size="pagination.size"
          :total="pagination.total">
      </el-pagination>
    </div>

    <!--角色[新增/编辑]弹窗-->
    <el-dialog
        title="提示"
        :visible.sync="roleDialogVisible"
        width="50%"
        @close="dialogClosed">


      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-ruleForm">

        <el-form-item label="角色名称" prop="username" label-width="100px">
          <el-input v-model="editForm.username" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="唯一编码" prop="code" label-width="100px">
          <el-input v-model="editForm.code" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="描述" prop="description" label-width="100px">
          <el-input v-model="editForm.description" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="状态" prop="status" label-width="100px">
          <el-radio-group v-model="editForm.status">
            <el-radio :label=0>禁用</el-radio>
            <el-radio :label=1>正常</el-radio>
          </el-radio-group>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="submitForm('editForm')">提交</el-button>
          <el-button @click="resetForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

    <!--角色[赋权]弹窗-->
    <el-dialog
        title="角色赋权"
        :visible.sync="permDialogVisible"
        width="50%"
        @close="dialogClosed">

      <el-form :model="permForm" ref="editForm" label-width="100px" class="demo-ruleForm">
        <el-tree
            ref="permTree"
            :data="permForm.treeData"
            show-checkbox
            :default-expand-all="true"
            node-key="id"
            :props="permForm.defaultProps">
        </el-tree>
      </el-form>

      <!--底部确定和取消按钮-->
      <span slot="footer" class="dialog-footer">
          <el-button @click="permDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="permFormSubmitHandle()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SysRole",
  data() {
    return {
      //搜索框
      searchForm: {
        name: ''
      },
      //新增、修改角色的表单
      editForm: {},
      //赋权表单
      permForm: {
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      //角色展示表格
      tableData: [],
      multipleSelection: [],
      //弹窗开关
      roleDialogVisible: false,
      permDialogVisible: false,
      //分页
      pagination: {
        total: 0,
        size: 10,
        current: 1
      },
      //角色表单校验规则
      editFormRules: {
        username: [
          {required: true, message: '请输入角色名称', trigger: 'blur'}
        ],
        code: [
          {required: true, message: '请输入唯一编码', trigger: 'blur'}
        ],
        status: [
          {required: true, message: '请选择状态', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    //1.取到表格的值
    this.getRoleList();
    //2.取得权限的数据
    this.$axios.get('/sys/menu/list').then(res => {
      console.log(res);
      this.permForm.treeData = res.data.data;
    });
  },
  methods: {
    /**
     * 表格
     **/
    //获取表格数据
    getRoleList() {
      this.$axios.get('/sys/role/list', {
        //如果表格和分页有值传过去
        params: {
          name: this.searchForm.name,
          pagination: {
            current: this.pagination.current,
            size: this.pagination.size
          }
        }
      }).then(
          res => {
            this.tableData = res.data.data.records;
            this.pagination = res.data.data.pagination;
          }
      )
    },
    //给角色行分配权限
    editRowPermHandle(rowId) {
      //1.展示权限对话框
      this.permDialogVisible = true;
      //2.获取该角色的权限
      this.$axios.get('/sys/role/info/' + rowId).then(res => {
        console.log(res);
        //2.1将其有的权限渲染到权限是树上
        this.$refs['permTree'].setCheckedKeys(res.data.data.menuIds);
        //2.2 把当前角色的权限数据赋值给权限表单保存
        this.permForm.personalData = res.data.data;
      });
    },
    //赋权表单提交处理
    permFormSubmitHandle() {
      //获取选中的权限树的数据
      //树控件里面属性可以设置每个节点上面保存的是什么数据
      //如果我在控件上设置node-key="id",那么我调用getChecckedKeys()获取的就是id
      // this.permForm.personalData.menuIds = this.$refs.permTree.getCheckedKeys();
      let checkedMenuIds = this.$refs.permTree.getCheckedKeys();
      //向后台发起权限更新请求
      this.$axios.post('/sys/role/perm/' + this.permForm.personalData.id, checkedMenuIds).then(
          res => {
            //这里没有根据业务code判断
            this.$message.success('赋权成功!');
            //刷新角色表的数据
            this.getRoleList();
            //关闭赋权表单对话框
            this.permDialogVisible = false;
          }
      );
    },
    //编辑表格的某一行
    editRow(rowId) {
      //需要调用api获取最新数据
      this.$axios.get('/sys/role/info/' + rowId).then(res => {
        //拿去数据填充到编辑表单中
        console.log(res);
        this.editForm = res.data.data;
        //获取成功之后显示编辑表单对话框
        this.roleDialogVisible = true;
      });
    },
    //处理删除角色请求，可以是单条也可以是多条
    delRoleHandle(id) {
      let ids = [];
      //处理多条删除
      this.multipleSelection.forEach(row => {
        ids.push(row.id)
      });
      //处理单条删除
      if (id !== undefined)
        ids.push(id)

      //打印ids
      // console.log(ids);

      //发起请求
      this.$axios.post('/sys/role/delete', ids).then(res => {
        //获取成功之后显示编辑表单对话框
        this.$message({
          message: '删除成功！',
          type: 'success',
          showClose: true,
          duration: 700,
          //消息弹窗关闭回调
          onClose: () => {
            //获取最新数据
            this.getRoleList();
          }
        });
      });
    },
    //表格切换选中
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    //选中改变的时候
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    /**
     *分页控件
     **/
    //分页条数改变的回调
    handleSizeChange(size) {
      this.pagination.size = size;
      this.getRoleList();
    },
    //分页当前页码改变的回调
    handleCurrentChange(current) {
      this.pagination.current = current;
      this.getRoleList();
    },
    /**对话框**/
    //对话框关闭回调
    dialogClosed() {
      console.log('dialogClosed!!!');
      this.editForm = {}
    },
    /**角色表单**/
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //根据表单元素中是否含有id判断当前表单是更新还是添加
          this.$axios.post('/sys/role/' + (this.editForm.id ? 'update' : 'save')).then(
              res => {
                this.$message({
                  message: '恭喜你，提交成功！',
                  type: 'success',
                  showClose: true,
                  duration: 700,
                  //消息消失或关闭的回调
                  onClose: (msgIns) => {
                    //1.更新表格
                    this.getRoleList();
                    //2.关闭对话框
                    this.roleDialogVisible = false;
                    //3.清空表单数据,消息关闭回调的时候处理

                    //并不是将表单中的props直接清空
                    //将所有字段值重置为开始修改之前的初始值并移除校验结果
                    //在这可调可不调
                    // this.resetForm(formName);
                    // 需要手动清空表单
                    // this.editForm = {};
                  }
                });
              }
          )

        } else {
          return false;
        }
      });
    },
    //重置表单项到打开时候的状态
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
.el-pagination {
  float: right;
  margin-top: 22px;
}
</style>
