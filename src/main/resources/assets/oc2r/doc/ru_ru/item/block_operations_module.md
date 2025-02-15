# Модуль управления блоками
![Сломай и быстро замени](item:oc2r:block_operations_module)

Модуль управления блоками позволяет [роботам](robot.md) ломать и ставить блоки в игровом мире.

## API
Название устройства: `block_operations`

Это устройство с высокоуровневым API. Оно может управляться через Lua в стандартном дистрибутиве Linux. Например:
`local d = require("devices")`  
`local m = d:find("block_operations")`  
`m:excavate("front")`

### Стороны
Стороны указываются относительно лицевой стороны робота. Допускаются значения: `front`, `up` и `down`.

### Методы
`excavate([side]):boolean` робот пытается сломать блок в указанном направлении. Подобранные блоки размещаются в слотах начиная с текущего. Если он заполнен, блоки буду складываться в следующий и так далее. Если инвентарь полон, робот не будет подбирать выпавшие вещи.
- `side` сторона, на которой робот будет ломать блок. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает статус выполненной задачи (`true` - удача, `false` - неудача).

`place([side]):boolean` робот пытается поставить блок в указанном направлении. Блок поставится из текущего слота. Если он пуст, робот ничего не поставит.
- `side` сторона, на которой робот будет ставить блок. Опциональное значение, по умолчанию равно `front`. Смотрите секцию "Стороны".
- Возвращает статус выполненной задачи (`true` - удача, `false` - неудача).

`durability():number` возвращает оставшуюся прочность модуля. Если она достигнет нуля, робот не сможет ломать блоки, пока модуль не починят.
- Возвращает оставшуюся прочность модуля в числовом виде.

`repair():boolean` пытается починить модуль, используя материалы в текущем слоте. Метод берет по одному предмету за раз. Источником материала может служить любой добывающий инструмент вроде кирки или лопаты. Качество предмета напрямую влияет на количество восстанавливаемой прочности модуля.
- Возвращает статус успешности починки (`true` - удача, `false` - неудача).
