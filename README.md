# PermissionAnnotation

- **AOP框架实现**
- **完全注解使用**

## 引入

先在根目录build.gradle的dependencies里添加
```gradle
dependencies {
        ...
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
}
```
Sync一下，然后在app的build.gradle顶部加上
```gradle
apply plugin: 'android-aspectjx'
```
最后在下方dependencies里加上
```gradle
dependencies {
        ...
        implementation ('com.su:PermissionAnnotation:1.0.1')
}
```

## 使用

PermissionAnnotation目前有三种注解，分别是
```Java
@Permission
@APermission
@PermissionDenial
```
### @Permission和@APermission

`@Permission`和`@APermission`都是申请权限的注解，不同的地方在于
```Java
@Permission 
//注解的方法可以有返回值，但无论申请成功与否都会执行

@APermission 
//注解的方法不可有返回值，但它可以根据申请情况决定是否调用，也就是只有成功申请到权限才会成功调用
```
两个注解均支持同时申请多个权限，且支持指定requestCode。具体属性值如下

属性          | 类型      | 说明                 |默认值
------------- | ---------| -------------------- |---------------
value         | String[] | 存放权限             | 无，必须指定
 requsetCode  | int      | 申请权限时的请求码    |100/200
denialMsg     | String   | 默认拒绝提示显示的msg | [DEFAULT_MSG](https://github.com/Ryensu/PermissionAnnotationDemo/blob/master/permissionannotation/src/main/java/com/su/permissionannotation/Permission/PermissionUtils.java)

在需要申请权限的方法头上加`@Permission`或`@APermission`即可在被调用时申请权限，如
```Java
@Permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private int testMethod0(int t) {
        Log.d("测试", "testMethod0" + t);
        return t;
    }

@Permissions(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, denialMsg = "测试msg")
    public int testMethod(int t) {
        Log.d("测试", "testMethod" + t);
        return t;
    }

@APermissions(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, requestCode = 111)
    private void testMethod2(int t) {
        Log.d("测试", "testMethod2" + t);
    }
```

### @PermissionDenial
`@PermissionDenial`是权限拒绝回调注解。被`@PermissionDenia`注解的方法会在被用户拒绝授予权限时执行（若勾选了不再提示则不会调用），如
```Java
@PermissionDenial(requestCode = 111)
    private void testMethod2Denial() {
        Toast.makeText(this, "testMethod2方法申请权限被拒绝的回调方法", Toast.LENGTH_SHORT).show();
    }
```
该注解必须指定requsetCode且`不能为默认值100/200`&&`应该>0`以和对应的权限申请操作绑定
### 默认拒绝回调
若在使用`@Permission`或`@APermission`时不指定requsetCode则会走默认提供的拒绝提示
```Java
    //默认权限说明
    public static void showDefaultMsg(Context context, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("权限说明")
                .setMessage(msg)
                .setNegativeButton("了解", null)
                .create();
        alertDialog.show();
    }
```
其中`msg`就是具体说明，如果在使用申请权限的注解时不指定`denialMsg`则显示默认值[DEFAULT_MSG](https://github.com/Ryensu/PermissionAnnotationDemo/blob/master/permissionannotation/src/main/java/com/su/permissionannotation/Permission/PermissionUtils.java)

基本情况就是这样了，欢迎star

### 其他
[使用的AOP框架](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)