# 红石接口
![一切红色](block:oc2r:redstone_interface)

红石接口提供了一个全方向的总线，用于收发红石信号。

使用此设备可以与简易的设备互动，如门和灯，或其他提供基于红石的协议的机械。

这是一个高级设备。必须使用高级设备API进行控制。默认的Linux发行版提供了用于此API的Lua库。例如：  
`local d = require("devices")`  
`local r = d:find("redstone")`  
`r:setRedstoneOutput("up", 15)`

## API
设备名称：`redstone`

### 面
下列方法中表示面的参数均指设备方块的某个面。有效值包括：`up`、`down`、`left`、`right`、`front`、`back`、`north`、`south`、`west`、`east`。

方块的每个面都有标记，以方便识别。面的名称对应从主面（只有一个记号）观察时的名称。当看向主面时：
- `front`和`south`为我们正看向的面。
- `back`和`north`为方块的背面。
- `left`和`west`为我们左边的面。
- `right`和`east`为我们右边的面。
- `up`和`down`为顶部和底部的面。

### 方法
`getRedstoneInput(side):number` 获取指定面接收到的红石信号。
- `side`表示要获取输入的面的字符串。参见“面”章节。
- 返回一个表示当前输入信号强度的数字。

`setRedstoneOutput(side, value:number)` 设置指定面发出的红石信号。
- `side`表示要设置信号的面的字符串。参见“面”章节。
- `value`为一个表示信号强度的数字，范围为[0, 15]。

`getRedstoneOutput(side):number` 获取指定面发出的红石信号。
- `side`表示要获取输出的面的字符串。参见“面”章节。
- 返回一个表示当前输出信号强度的数字。
