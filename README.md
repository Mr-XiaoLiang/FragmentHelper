# FragmentHelper
Fragment的辅助管理工具

| 特性      | ViewGroup | ViewPager | ViewPager2 |
| :-- | :--: | :--: | :--: |
| 动态添加或移除Fragment | ⭕️ | ⭕️ | ⭕️ |
| 兼容FragmentManager的自动恢复机制 | ⭕️ | ⭕️ | ⭕️ |
| 动态更新Fragment的参数 | ⭕️ | ❌ | ❌ |
| 查找已存在的Fragment | ⭕️ | ⭕️ | ⭕️ |
| 懒加载 | ⭕️ | ⭕️ | ⭕️ |
| 生命周期管理 | ⭕️ | ⭕️ | ⭕️ |
| 初始化参数管理 | ⭕️ | ⭕️ | ⭕️ |
| 参数拦截补充 | ⭕️ | ⭕️ | ⭕️ |
| 返回栈 | ⭕️ | ❌ | ❌ |
| 动态修改指定指定位置的Fragment | ⭕️ | ❌ | ⭕️ |

## 简单使用
```kotlin
    class Demo: Activity() {

        fun onCreate(savedInstanceState: Bundle?) {
            ...
            val switcher = FragmentHelper.with(this)
                .add(ViewPageDemoFragment::class.java, TAG_VIEWPAGER)
                .add(ViewPage2DemoFragment::class.java, TAG_VIEWPAGER2)
                .add(TempDemoFragment::class.java, TAG_TEMP)
                .add(TempDemoFragment::class.java, TAG_TEMP2)
                .bind(binding.fragmentContainerView)

            switcher.switchTo(TAG_VIEWPAGER2)
            ...
        }

    }

```

## API
### FragmentHelper
```kotlin
 // 依赖Activity的FragmentManager创建一个构造工具
fun with(activity: AppCompatActivity): Builder

// 依赖Fragment的FragmentManager创建一个构造工具
fun with(fragment: Fragment): Builder
```

### FragmentHelper.Builder
```kotlin
/**
 * 简单添加一个Fragment信息
 * @param fragment Fragment的类信息，需要提供无参构造器
 * @param pageKey 这个Page的Key，它是唯一的，因为你需要用它来切换页面
 */
fun add(fragment: Class<out Fragment>, pageKey: String): Builder

/**
 * 添加一个完整的Fragment描述信息
 */
fun add(info: FragmentInfo): Builder

/**
 * 绑定到一个ViewPager上
 * @param viewGroup ViewPager对象
 * @param stateEnable true表示使用FragmentStatePagerAdapter，否则使用FragmentPagerAdapter
 */
fun bind(viewGroup: ViewPager, stateEnable: Boolean = false): ViewPagerHelper.V1

/**
 * 绑定到一个ViewPager2上
 * @param viewGroup ViewPager2实例
 */
fun bind(viewGroup: ViewPager2): ViewPagerHelper.V2

/**
 * 添加到一个普通的ViewGroup上，推荐使用FrameLayout或FragmentContainerView
 * @param viewGroup Fragment容器
 */
fun bind(viewGroup: ViewGroup): FragmentSwitcher

/**
 * 添加一个Fragment初始化的监听器
 * 可以通过它在Fragment实例化时添加参数信息
 * @param callback Fragment实例化的回调函数
 */
fun onFragmentInit(callback: FragmentInitCallback): Builder

/**
 * 当Fragment当参数发生变化时的回调函数
 * 它会在FragmentInitCallback之后触发
 * 也可能发生在switchTo时
 * @param callback Fragment参数改变的回调函数
 */
fun onArgumentsChanged(callback: FragmentArgumentsChangedCallback): Builder
```