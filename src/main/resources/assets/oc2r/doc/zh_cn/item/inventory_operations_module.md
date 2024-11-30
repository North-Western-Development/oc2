# 物品栏操作模块
![你的就是我的](item:oc2r:inventory_operations_module)

物品栏操作模块为[机器人](robot.md)提供了向世界中的物品容器放入和从中取出物品的能力。它同时支持方块和实体的物品栏。

## API
设备名称：`inventory_operations`

这是一个高级API设备。它可以使用默认Linux发行版中的Lua进行控制。例如：  
`local d = require("devices")`  
`local m = d:find("inventory_operations")`  
`m:drop(1, "front")`

### 面
以下方法表示面的参数指相对于机器人视角的方向。有效值包括：`front`、`up`、`down`。

### 方法
`move(fromSlot:number, intoSlot:number, count:number)` 尝试将指定数量的物品从一个机器人物品栏的一个槽位移动到另一个槽位。
- `fromSlot`为提取物品的槽位。
- `intoSlot`为插入物品的槽位。
- `count`为要移动的物品数量。

`drop(count:number[,side]):number` 尝试从指定槽位提取，朝指定方向丢弃物品。它会将物品丢入物品容器中，如果没有物品容器存在则会丢到世界中。
- `count`为要丢弃的物品数量。
- `side`为丢弃物品的相对方向。可选，默认为`front`。参见“面”章节。
- 返回丢弃的物品数量。

`dropInto(intoSlot:number, count:number[,side]):number` 尝试从指定槽位提取物品，朝指定方向丢入物品容器的指定槽位。它只会将物品丢入物品容器中。
- `intoSlot`为要插入物品的槽位。
- `count`为要丢弃的物品数量。
- `side`为丢弃物品的相对方向。可选，默认为`front`。参见“面”章节。
- 返回丢弃的物品数量。

`take(count:number[,side]):number` 尝试从指定方向获取指定数量的物品。它会从物品容器中获取物品，如果没有物品容器存在则会从世界中获取物品。
- `count`为要获取的物品数量。
- `side`为获取物品的相对方向。可选，默认为`front`。参见“面”章节。
- 返回获取的物品数量。

`takeFrom(fromSlot:number, count:number[,side]):number` 尝试从指定方向的物品容器的指定槽位获取指定数量的物品。它只会从物品容器中获取物品。
- `fromSlot`为获取物品的槽位。
- `count`为要获取的物品数量。
- `side`为获取物品的相对方向。可选，默认为`front`。参见“面”章节。
- 返回获取的物品数量。
