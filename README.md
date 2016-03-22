# ContactPicker

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

##介绍 Introduction##
**ContactPicker** - 仿 Android 联系人列表界面的 Demo ，具有快速索引功能和搜索功能。(The Demo of imitation Android contact list UI with fast indexing and search capabilities.)

##截图 Screenshot##
![screenshot_gif](https://github.com/yuqirong/ContactPicker/blob/master/screenshots/screenshots.gif)

##下载 Demo Download##
安装后请确保该应用有读取联系人权限。(After installation make sure that the application has permission to read contacts.)

[Download](https://github.com/yuqirong/ContactPicker/blob/master/screenshots/app-debug-unaligned.apk)

##用法 Usage##
###step 1###
把QuickIndexBar控件添加到你的布局文件中。(Include the QuickIndexBar widget in your layout.)

	<com.yuqirong.contactpicker.view.QuickIndexBar
        android:id="@+id/qib"
        android:layout_width="25dp"
        android:layout_height="match_parent"
        android:layout_alignParentRight="true"
        android:background="#aa000000" />

###step 2###
可以在`onCreate(Bundle savedInstanceState)`中添加`OnIndexChangeListener`。具体使用方法可以参考代码。(You can add the `OnIndexChangeListener` for `QuickIndexBar` in `onCreate(Bundle savedInstanceState)`.Specific methods can refer to the code.)

	qib.setOnIndexChangeListener(new QuickIndexBar.OnIndexChangeListener() {
			@Override
			public void onIndexChange(int section) {
				...
			}

			@Override
			public void onActionUp() {
				...
			}
		});

好了，享受吧！(Well, enjoy it!)

##联系方式 Contact Me##
新浪微博 Sina Weibo：[@活得好像一条狗](http://weibo.com/yyyuqirong) 

电子邮箱 Email：<yqr271228943@gmail.com>

If you have any questions or want to contact me,you can also leave a message in [Issues](https://github.com/yuqirong/ContactPicker/issues).

##开源许可证 License##

    Copyright (c) 2016 yuqirong 

    Licensed under the Apache License, Version 2.0 (the "License”);
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
