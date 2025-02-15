# 磁盘驱动器
![旋转一下](block:oc2r:disk_drive)

磁盘驱动器提供了快速媒体交换的选项。与 [硬盘](../item/hard_drive.md) 不同，[软盘](../item/floppy.md) 可以在运行时添加和移除。

请注意，在从驱动器中移除软盘之前，强烈建议显式卸载软盘，以避免数据丢失。

在 Linux 系统上，磁盘驱动器通常会作为 `/dev/vdX` 设备出现，紧随任何已安装的硬盘之后。它们不会被自动格式化或挂载，而只会作为原始块设备出现。要使用它们，你需要先进行配置。例如，在默认的 Linux 发行版上，以下命令将很有用：

- `mke2fs /dev/vdX` 用于格式化软盘。首次安装磁盘时运行此命令。*会清除软盘上的数据！*
- `mount /dev/vdX <挂载目录>` 在格式化软盘后挂载它。确保你要挂载到的目录存在且为空。
- `umount <挂载目录>` 卸载软盘。确保在从磁盘驱动器中移除软盘之前运行此命令，以避免数据丢失。

计算机 *必须关闭* 后才能安装或移除此组件。在计算机运行时安装它将没有效果，移除它可能会导致系统错误。
