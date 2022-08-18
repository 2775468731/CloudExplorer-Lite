import elementEnLocale from "element-plus/lib/locale/lang/en";
import fit2cloudEnLocale from "fit2cloud-ui-plus/src/locale/lang/en";
import subModuleEnLocale from "@/locales/lang/en";

const message = {
  commons: {
    home: "Home Page",
    notice: "Notice",
    to_do_list: "To Do List",
    view_all: "View All",
    operation: "Operation",
    name: "Name",
    tag: "Tag",
    org: "Organization",
    workspace: "Workspace",
    os: "Operating System",
    os_version: "Operating System Version",
    status: "Status",
    create_time: "Create Time",
    update_time: "Update Time",
    cloud_account: {
      native: "Cloud Account",
      name: "Cloud Account Name",
      data_center: "Data Center",
      cluster: "Cluster",
      region: "Region",
      zone: "Zone",
      host: "Host",
      storage: "Storage",
      disk: "Disk",
      vm: "Virtual Machine",
      image: "Image",
    },
    message_box: {
      alert: "Alert",
      confirm: "Confirm",
      prompt: "Prompt",
      confirm_delete: "Confirm Delete",
    },
    btn: {
      login: "Login",
      yes: "Yes",
      no: "No",
      ok: "OK",
      add: "Add",
      create: "Create",
      delete: "Delete",
      edit: "Edit",
      save: "Save",
      close: "Close",
      submit: "Submit",
      publish: "Publish",
      cancel: "Cancel",
      return: "Return",
      grant: "Grant",
      hide: "Hide",
      display: "Display",
      enable: "Enable",
      disable: "Disable",
      copy: "copy",
      sync: "Synchronize",
      view_api: "View API",
      prev: "Previous",
      next: "Next",
      switch_lang: "Switch Language",
      add_favorites: "Add To Favorites",
      cancel_favorites: "Cancel Favorites",
      search: "Search",
      refresh: "Refresh",
      import: "Import",
      export: "Export",
      upload: "Upload",
      download: "Download",
    },
    msg: {
      success: "{0}Success",
      op_success: "Success",
      save_success: "Save Success",
      delete_success: "Delete Success",
      fail: "{0} Failed",
    },
    validate: {
      limit: "Length Is Between {0} 到 {1}",
      input: "Please Input{0}",
      select: "Please Select{0}",
      confirm_pwd: "The Two Passwords You Entered Were Inconsistent",
    },
    personal: {
      personal_info: "Personal Detail",
      edit_pwd: "Edit Password",
      help_document: "Help Document",
      exit_system: "Exit System",
      old_password: "Old Password",
      new_password: "New Password",
      confirm_password: "Confirm Password",
      login_identifier: "Login Identifier",
      username: "Username",
      phone: "Phone",
      wechat: "Wechat",
    },
    date: {
      select_date: "Select Date",
      start_date: "Start Date",
      end_date: "End Date",
      select_time: "Select Time",
      start_time: "Start Time",
      end_time: "End Time",
      select_date_time: "Select Date Time",
      start_date_time: "Start Date Time",
      end_date_time: "End Date Time",
      range_separator: "To",
      date_time_error: "Start Time Can Not Be Greater Than End Time",
    },
    login: {
      username: "Username",
      password: "Password",
      title: "CloudExplorer Cloud Service Platform",
      welcome:
        "Welcome Back, Please Enter Your User Name and Password to Login",
      expires: "The Authentication Information Has Expired, Please Login Again",
    },
  },
};

export default {
  ...elementEnLocale,
  ...fit2cloudEnLocale,
  ...message,
  ...subModuleEnLocale,
};
