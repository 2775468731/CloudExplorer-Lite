export default [
  {
    name: "user_manage",
    title: "用户与租户",
    componentPath: "",
    path: "/user_manage",
    icon: "corporate_fare",
    order: 1,
    children: [
      {
        name: "user",
        title: "用户管理",
        icon: "yonghuguanli",
        path: "/user",
        componentPath: "/src/views/UserManage/index.vue",
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
        ],
        order: 1,
      },
      {
        name: "role",
        title: "角色管理",
        icon: "jiaoseguanli",
        path: "/role",
        componentPath: "/src/views/RoleManage/index.vue",
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
        ],
        order: 2,
      },
      {
        name: "org",
        title: "组织管理",
        icon: "zuzhiguanli",
        path: "/org",
        componentPath: "/src/views/OrgManage/index.vue",
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
        ],
        order: 3,
      },
      {
        name: "workspace",
        title: "工作空间",
        icon: "gongzuokongjian",
        path: "/workspace",
        componentPath: "/src/views/WorkspaceManage/index.vue",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: ["WORKSPACE_MENU_VIEW"],
          },
        ],
        order: 4,
      },
    ],
  },
  {
    title: "系统设置",
    icon: "shezhi",
    name: "system_setting",
    path: "/system_setting",
    order: 2,
    children: [
      {
        title: "系统设置",
        icon: "shezhi",
        name: "system_module",
        componentPath: "/src/views/SystemModule/index.vue",
        path: "/system_module",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 1,
      },
      {
        title: "显示设置",
        icon: "xianshi",
        name: "view_setting",
        componentPath: "/src/views/ViewSetting/index.vue",
        path: "/view_setting",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 2,
      },
      {
        title: "消息通知",
        icon: "xiaoxi",
        name: "message",
        componentPath: "/src/views/Message/index.vue",
        path: "/message",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 3,
      },
      {
        title: "系统日志",
        icon: "zhuanxierizhi",
        name: "system_log",
        componentPath: "/src/views/SystemLog/index.vue",
        path: "/system_log",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 4,
      },
      {
        title: "关于",
        icon: "guanyu",
        name: "about",
        componentPath: "/src/views/About/index.vue",
        path: "/about",
        requiredPermissions: [
          {
            role: "ORG",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 5,
      },
    ],
  },
];
