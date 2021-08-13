import Vue from 'vue';


// 为自定义的选项 'myOption' 注入一个处理器。
Vue.mixin({
    methods: {
        hasPerm(perm){
            let perms = this.$store.state.user.permList;
            return perms.indexOf(perm) > -1;
        }
    }
})
