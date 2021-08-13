const getDefaultState = () => {
    return {
        editableTabsValue: 'Index',
        editableTabs: [{
            title: '首页',
            name: 'Index'
        }]
    }
}
const menu = {
    state: getDefaultState(),
    mutations: {
        //标签页导航添加tab
        ADD_TAB(state,tab) {
            //找到是不是已经存在了该tab，存在不添加
            let index = state.editableTabs.findIndex(e => e.name === tab.name);
            if (index === -1) {
                state.editableTabs.push({
                    title: tab.title,
                    name: tab.name
                });
            }
            state.editableTabsValue = tab.name;
        },
        RESET_MENU_STATE(state){
            Object.assign(state,getDefaultState())
        }
    },
    actions: {}
}

export default menu;
