# vmvl memo

### ViewModel & ViewLogic

Presentation層を**ViewModel(何を表示して、何を受け取るか)**と**ViewLogic(どう表示するか、どう受け取るか)**に分ける.

+ ViewModel   
-> ViewModelActivity (or ViewModelFragment) の派生クラス
+ ViewLogic   
-> ActivityViewLogic (or FragmentViewLogic)　の派生クラス

ViewModelでは,何を表示して,何を受け取るのかを定義する.これらは,

+ 何を表示するか   
-> ActivityViewLogicInterface (or FragmentViewLogicInterface) の派生Interface
+ 何を受け取るか   
-> ViewModelInterface の派生Interface

にそれぞれ定義する.  
ViewModelはViewModelInterfaceを実装し、内部にViewLogicInterface型のメンバを持つ.  
ViewLogicはViewLogicInterfaceを実装し、内部にViewModelInterface型のメンバを持つ.
  
  
ViewModelは表示するデータをViewLogicにInterfaceを介して投げる.逆に,ViewLogicは,ユーザー入力があればそのデータをViewModelにInterfaceを介して投げる.
ViewLogicはActivity(Fragment)と同じライフサイクルを持つ.なので,Activity(Fragment)に書いていたUI周りの処理をそのままViewLogicに書くイメージ.
    
--------------------------------------------------------------------------------------------------------

### SupportInterface

+ SupportAVLInterface, SupportFVLInterface  
 -> ViewLogicがActivityやFragment固有のメソッド(findViewById()など)を使用できるようにするためのInterface (package private)
 
 ViewModelActivity(ViewModelFragment)に実装されていて,固有なメッソド１つにつき１つのアクセス用メッソドが用意されている.これらのアクセス用メッソドはViewLogicのデリゲートメッソドから呼ばれる.
 
##### 例: findViewById(int id)
ActivityViewLogic.java

```
// デリゲートメッソド
View findViewById(int id) {
	return mSupportAVLInterface.onFindViewByIdMethodDispatch(id);
}
```

SupportAVLInterface.java

```
// アクセス用メッソド
View onFindViewByIdMethodDispatch(int id);
```

ViewModelActivity.java

```
// アクセス用メッソドの実装
onFindViewByIdMethodDispatch(int id) {
	return findViewById(id);
}
```