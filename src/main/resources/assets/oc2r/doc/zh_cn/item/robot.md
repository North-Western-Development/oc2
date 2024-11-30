# 机器人
![我，代表我自己，欢迎我们的新机器人统治者](item:oc2r:robot)

机器人本质上是移动的[电脑](../block/computer.md)。由于非固定的性质，其行为与常规电脑有所不同。它们不能连接[总线接口](../block/bus_interface.md)。机器人支持模块设备，若不是扩展卡设备。模块设备专门考虑了机器人的移动性。

机器人具有固定大小的物品栏，装有最先进的能量容器。只有机器人的常规物品栏可以通过像漏斗这样的设备自动填充和清空。机器人的组件物品栏必须手动配置。

在默认配置下，机器人无法与自己的物品栏进行交互。[物品栏操作模块](inventory_operations_module.md)可以使机器人在自己的物品栏中移动物品，以及向其他物品栏中放入或从中提取物品。

要为机器人充电，建议使用[充电机](../block/charger.md)。机器人只需移动到充电机上方即可为自身充电。也可以将它们可放置到充电机上方的物品容器中。

默认的Linux发行版提供了一个Lua实用程序库`robot`，简化了对机器人的控制。底层API提供了异步移动机器人的方法。该库实现了同步的版本，更便于编写有顺序要求的程序。

## API
设备名称：`robot`

这是一个高级API设备。可以在默认的Linux发行版中使用Lua控制。例如：  
`local d = require("devices")`  
`local r = d:find("robot")`  
`r:move("forward")`

### 方向
以下方法中表示方向的参数指相对于机器人的方向。有效值为：`forward`、`backward`、`upward`、`downward`（用于移动操作），`left`、`right`（用于旋转操作）。这些方向始终是相对于机器人执行操作时视角的相对方向。

为了方便，可以使用这些值的简短别名：`back`、`up`、`down`。为了极致简洁，也可以使用各个方向的首字母。

### 方法
这些方法在底层的机器人设备上可用。注意，库为所有这些方法提供了有用的封装。建议使用库而不是直接与设备交互。

`getEnergyStored():number` 返回机器人内部能量存储器的当前存储量。
- 返回储存的能量数量。

`getEnergyCapacity():number` 返回机器人内部能量存储器的最大存储量。
- 返回储存的最大能量数量。

`getSelectedSlot():number` 返回当前选定的机器人物品栏槽位。这被许多模块作为隐式输入使用。
- 返回所选物品栏槽位的索引号。

`setSelectedSlot(slot:number):number` 设置当前选定的机器人物品栏槽位。这被许多模块作为隐式输入使用。
- `slot`为要选定的物品栏槽位的索引号。
- 返回新选定的槽位的索引号。如果指定的值无效，则可能与`slot`不同。

`getStackInSlot(slot:number):table` 获取指定槽位中的物品描述。
- `slot`为要获取物品描述的槽位的索引号。

`move(direction):boolean` 尝试将向指定方向的移动操作加入操作队列。
- `direction`为要移动的方向。
- 返回操作是否成功入队。

`turn(direction):boolean` 尝试将转动操作加入操作队列。
- `direction`为要转动的方向。
- 返回操作是否成功入队。

`getLastActionId():number` 返回最后入队的操作的不透明ID。成功调用`move()`或`turn()`后调用此方法，以获取与入队成功的操作相关的ID。
- 返回最后入队的操作的ID。

`getQueuedActionCount():number` 返回当前操作队列中等待处理的操作数量。使用此方法可以在入队失败时等待操作完成。
- 返回当前待处理的操作数量。

`getActionResult(actionId:number):string` 返回指定ID对应操作的结果。操作ID可以用`getLastActionId()`获取。只可查询有限数量的历史操作结果可用。
- 返回指定ID对应操作的结果，若不可用则无返回值。当可用时，可能的值包括：`INCOMPLETE`（未完成）、`SUCCESS`（成功）、`FAILURE`（失败）。

### 库API
- 库名称：`robot`

这是一个Lua库。可以在默认的Linux发行版中使用。例如：  
`local r = require("robot")`  
`r.move("forward")`  
`r.turn("left")`

### 方法
`energy():number` 返回机器人内部能量存储器的当前存储量。
- 返回储存的能量数量。

`capacity():number` 返回机器人内部能量存储器的最大存储量。
- 返回储存的最大能量数量。

`slot():number` 返回当前选定的机器人物品栏槽位。这被许多模块作为隐式输入使用。
- 返回所选物品栏槽位的索引号。

`slot(slot:number):number` 设置当前选定的机器人物品栏槽位。这被许多模块作为隐式输入使用。
- `slot`为要选定的物品栏槽位的索引号。
- 返回新选定的槽位的索引号。如果指定的值无效，则可能与`slot`不同。

`stack([slot:number]):table` 获取指定槽位中的物品描述。
- `slot`为要获取物品描述的槽位的索引号。可选，默认为 `slot()`。

`move(direction):boolean` 尝试移动到指定方向。阻塞程序，直到移动操作完成。
- `direction`为要移动的方向。
- 返回操作是否成功。

`moveAsync(direction)` 尝试异步移动到指定方向。阻塞程序，直到操作成功入队。
- `direction`为要移动的方向。

`turn(direction):boolean` 尝试朝指定方向转动。阻塞程序，直到旋转操作完成。
- `direction`为要转动的方向。
- 返回操作是否成功。

`turnAsync(direction)` 尝试异步转动到指定方向。阻塞程序，直到操作成功入队。
- `direction`为要转动的方向。
