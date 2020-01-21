Viewbinding
===========

**Attention**: Please consider switching to [view binding][3] in the coming months.

[ ![Download](https://api.bintray.com/packages/mickverm/maven/Viewbinding/images/download.svg) ](https://bintray.com/mickverm/maven/Viewbinding/_latestVersion)

[ButterKnife][1]-esque view binding for Kotlin based on [KotterKnife][2].

```kotlin
public class PersonView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs){
    val firstName: TextView by bindView(R.id.tv_first_name)
    val lastName by bindView<TextView>(R.id.tv_last_name)

    // Optional binding
    val middleName: TextView? by bindOptionalView(R.id.tv_middle_name)

    // List binding
    val nameViews: List<TextView> by bindViews(R.id.tv_first_name, R.id.tv_last_name)

    // List binding with optional items being omitted
    val nameViews: List<TextView> by bindOptionalViews(R.id.tv_first_name, R.id.tv_middle_name, R.id.tv_last_name)
}
```

These methods are available on subclasses of `View`, `Activity`, `Dialog`, `DialogFragment`, `Fragment` and RecyclerView's `ViewHolder`.

Download
--------

````groovy
implementation 'be.mickverm.viewbinding:viewbinding:1.0.2'
````

License
-------

    Copyright 2020 Michiel Vermeersch

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://jakewharton.github.io/butterknife
[2]: https://github.com/JakeWharton/kotterknife
[3]: https://developer.android.com/topic/libraries/view-binding
