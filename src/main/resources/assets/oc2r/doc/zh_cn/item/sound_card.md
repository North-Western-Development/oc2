# 声卡
![少一些寂静之声](item:oc2r:sound_card)

声卡可以回放其丰富且逼真的样本库中的各种声音效果。由于程序内部在工程上的限制，连续播放音效时会有短暂的暂停。在此时间窗口内尝试播放其他音效将没有效果。

这是一个高级设备。必须使用高级设备API进行控制。默认的Linux发行版提供了该API的Lua库。例如：  
`local d = require("devices")`  
`local s = d:find("sound")`  
`s:playSound("entity.creeper.primed")`

## API
设备名称：`sound`

### 方法
`playSound(name:string[,volume:float,pitch:float])` 播放指定名称的声音效果。
- `name`为要播放的音效名称。
- `volume`为播放音效的音量，范围从`0`到`1`，`1`为正常音量。可选，默认为 `1`。
- `pitch`为播放效果的音高，范围从`0.5`到`2`，`1`为正常音高。可选，默认为 `1`。
- 如果指定的名称无效，则会抛出异常。

`findSound(name:string):table` 返回匹配给定名称的可用声音效果列表。请注意，结果数量有限，因此过于泛指的查询会导致结果被截断。
- `name`为要查询的名称查询。
- 返回与所查询字符串匹配的声音效果名称列表。
