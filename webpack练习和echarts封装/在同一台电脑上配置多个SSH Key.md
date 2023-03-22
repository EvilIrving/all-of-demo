<!-- Permission denied (publickey) -->

# 在同一台电脑上配置多个SSH Key

`SSH Key` 是克隆代码的授权标识，全平台唯一，代表的是一个账号。每个平台账号配置的 `SSH key` 需要不同，才能标识到人，对应人员操作的权限和审计。在一些场景下，需要在同一台电脑上同时管理多个平台账号

## 设置 `SSH key`

1. 生成`SSH key`

    ```text
    ssh-keygen -t rsa -C "<您的邮箱>"
    Generating public/private rsa key pair.
    Enter file in which to save the key (~/.ssh/id_rsa):<为了区分多个key，请填写自定义的名称>
    ```

    > 说明 为了区分多个 `SSH key`，以上第3行请不要直接回车，请填写自定义的 key 路径名称以方便后续使用，如 `gitee_rsa`，`github_rsa`，然后点击回车确认。

2. 添加`id_rsa`

    ```text
    ssh-add ~/.ssh/id_rsa
    ```

    - 测试本地是否添加成功

    ```text
    ssh -T git@gitee.com
    ```

3. 添加到平台

    ```text
    cat ~/.ssh/id_rsa.pub
    ```

    登录平台账号A，点击页面右上角头像 - 个人设置 -「SSH 密钥」展现 SSH 密钥设置弹窗。将 `1` 中`*.pub`的公钥粘贴到输入框中保存。

    切换至平台账号B，将 `1` 中生成的另一个公钥同样配置到账号下。

4. 编辑~/.ssh/config文件

    ```text
    # 账号A
    Host gitee
    HostName gitee.com
    IdentityFile ~/.ssh/gitee_rsa
    PreferredAuthentications publickey
    IdentityAgent none
    IdentitiesOnly yes

    # 账号B
    Host github
    HostName github.com
    IdentityFile ~/.ssh/github_rsa
    PreferredAuthentications publickey
    IdentityAgent none
    IdentitiesOnly yes
    ```

    - HostName：填写托管平台服务地址；
    - Host：填写别名，方便命令行使用；
    - IdentityFile：填写证书位置；

5. 命令行操作

    在日常推拉代码工作时，获取代码库的 `ssh` 克隆地址后，因为本地有多个账号，可以手动选择使用哪个账号操作。

    以示例库举例，假设克隆地址为：`git@gitee.com:me/Codeup-Demo.git`。

    那么可以使用如下命令切换账号：

    ```text
    git@gitee:me/Codeup-Demo.git

    git@github:me/Codeup-Demo.git
    ```

## 报错

> Could not open a connection to your authentication agent
尝试重启SSH服务

1. 修改服务：

    以**管理员身份**运行命令提示符，并输入以下命令：

    ```text
    net stop sshd
    net start sshd
    ```

2. 使用服务管理器：

- 打开 Windows 服务管理器。可以在 Windows 搜索栏中搜索“服务”并点击打开。
- 找到 OpenSSH Authentication Agent 和 OpenSSH SSH Server。
- 右键点击服务并选择“重启”。

> Permission denied (publickey)

可能缺少第二步，重新按流程走一遍，如果不行，尝试以下：

- `cat /etc/ssh/sshd_config` 查看SSH服务对应配置文件，修改`PasswordAuthentication: no`为：`yes`
