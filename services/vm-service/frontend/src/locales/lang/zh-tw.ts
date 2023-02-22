const message = {
  vm_cloud_server: {
    label: {
      ip_address: "IP地址",
      info: "主機信息",
      vm: "雲主機",
      current_config: "當前配置",
      instance_type: "實例規格",
      new_config: "變更後配置",
      region: "區域/數據中心",
      zone: "可用區/集群",
      vm_tools_status: "VMTools狀態",
      vpc: "VPC",
      subnet: "子網",
      securityGroup: "安全組",
      expired_time: "到期時間",
      charge_type: "付費類型",
      project: "企業項目",
    },
    vm_tools_status: {
      not_Installed: "未安装",
      running: "運行中",
      not_running: "未運行",
    },
    status: {
      creating: "創建中",
      running: "運行中",
      stopped: "已關機",
      rebooting: "重啟中",
      wait_recycle: "待回收",
      deleted: "已刪除",
      failed: "失敗",
    },
    btn: {
      power_on: "啟動",
      shutdown: "關機",
      power_off: "關閉電源",
      reboot: "重啓",
      change_config: "配置變更",
    },
    message_box: {
      confirm_power_on: "確認啟動",
      confirm_shutdown: "確認關機",
      confirm_power_off: "確認關閉電源",
      confirm_reboot: "確認重啓",
      confirm_delete: "確認刪除",
      confirm_batch_operate: "確認執行批量{0}操作",
      check_vm_tools_status_confirm_shutdown:
        "當前虛擬機器未安裝VmTools或VmTools未運行，無法軟關機，若繼續操作則關閉電源，是否繼續？",
      confirm_config_update: "配置變更將會對實例執行關機操作，確認繼續?",
      confirm_delete_record: "確認刪除失敗記錄",
      confirm_batch_delete_record: "確認批量刪除失敗記錄",
    },
  },
  vm_cloud_image: {
    label: {
      management_info: "管理資訊",
      image_name: "鏡像名稱",
      image_id: "鏡像ID",
    },
    btn: {
      set_management_info: "設定管理資訊",
    },
  },
  vm_cloud_disk: {
    label: {
      vm: "所屬虛擬機器",
      size: "磁盤大小",
      disk_category: "磁片種類",
      disk_attribute: "磁片屬性",
      disk_type: "磁片類型",
      mount_info: "掛載資訊",
      delete_with_instance: "隨實例刪除",
      select_vm: "請選擇要掛載的雲主機",
      cloudVm: "雲主機",
      deleteWithVm: "隨雲主機刪除",
      disk_info: "磁盤信息",
      disk_name: "磁盤名稱",
      change_config: "配置變更",
      current_config: "當前配置",
      after_config: "變更後配置",
      disk_size: "磁盤大小",
      system_disk: "系統盤",
      data_disk: "數據盤",
      disk_id: "磁盤ID",
    },
    status: {
      creating: "創建中",
      in_use: "已掛載",
      available: "可用",
      enlarging: "擴容中",
      wait_recycle: "待回收",
      deleted: "已刪除",
    },
    btn: {
      create: "添加磁盤",
      enlarge: "擴容",
      uninstall: "卸載",
      mount: "掛載",
      delete: "刪除",
    },
    confirm: {
      detach: "確認將雲磁盤{0}從雲主機{1}上卸載",
      delete: "回收站未開啟，確認將雲磁盤{0}直接刪除",
      recycle: "回收站已開啟，確認將雲磁盤{0}放入回收站",
      batch_detach: "確認批量卸載雲磁盤",
      batch_delete: "回收站未開啟，確認批量刪除雲磁盤",
      batch_recycle: "回收站已開啟，確認批量將雲磁盤放入回收站",
    },
    msg: {
      canceled: "已取消{0}",
      select_one: "至少選擇一條記錄",
      datastore_null: "存儲器不能為空",
    },
  },
  recycle_bin: {
    batch_recover_tips: "是否恢復選中的資源？",
    batch_delete_tips: "是否徹底刪除選中的資源？刪除後將無法恢復！",
    recover_tips: "是否恢復資源[{0}]？",
    delete_tips: "是否徹底刪除[{0}]？刪除後將無法恢復！",
    recover: "恢復",
    delete: "徹底刪除",
    resource_name: "資源名稱",
    resource_type: "資源類型",
    workspace_name: "工作空間",
    resource_status: "狀態",
    relate_resource: "關聯資源",
    resource_config: "資源配置",
    user_name: "操作人",
    put_into_recycle_bin_time: "放入回收站時間",
    create_time: "創建時間",
    organization_name: "組織",
    ip_array: "IP地址",
  },
};
export default {
  ...message,
};
