<template>
  <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab" @tab-click="clickTab">
    <el-tab-pane
        v-for="(item, index) in editableTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  name: "Tabs",
  computed: {
    editableTabsValue: {
      get() {
        return this.$store.state.menu.editableTabsValue;
      },
      set(val) {
        this.$store.state.menu.editableTabsValue = val;
      }
    },
    editableTabs: {
      get() {
        return this.$store.state.menu.editableTabs;
      },
      set(val) {
        this.$store.state.menu.editableTabs = val;
      }
    }
  },
  methods: {

    removeTab(targetName) {
      //如果×的是首页，不进行删除
      if (targetName === 'Index') {
          return;
      }
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter(tab => tab.name !== targetName);

      //删除之后跳转到被激活的标签
      this.$router.push({name: activeName})
    },
    clickTab(target) {
      //根据路由名字来跳转
      this.$router.push({name: target.name})
    }
  }
}
</script>

<style scoped>

</style>
