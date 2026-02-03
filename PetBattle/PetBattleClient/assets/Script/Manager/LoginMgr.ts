import { JCEngine, JCEntity } from "../SDK/JCEngine";
import Player from "../Player/Player";
import GlobalData from "../Common/GlobalData";
import Camper from "../Common/Camper";
import JCTool from "../SDK/JCTool";
import ResourceMgr from "./ResourceMgr";

const { ccclass, property } = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {
    @property({ type: cc.Sprite })
    checkBoxSprite: cc.Sprite = null;
    @property({ type: cc.SpriteFrame })
    checkBoxFrames: cc.SpriteFrame[] = [];

    @property({ type: cc.EditBox })
    input_username: cc.EditBox = null;
    @property({ type: cc.EditBox })
    input_password1: cc.EditBox = null;
    @property({ type: cc.EditBox })
    input_password2: cc.EditBox = null;

    isLoginPanel: boolean = true;
    isRemember: boolean = false;

    onLoad() {
        cc.view.setOrientation(cc.macro.ORIENTATION_LANDSCAPE);
        this.checkRemember();

        let url = "ws://127.0.0.1:9999/petBattleServer";
        JCEngine.boot(url, Player);

        //if local test, suport input username and password by q or w
        cc.systemEvent.on(cc.SystemEvent.EventType.KEY_DOWN, (event: cc.Event.EventKeyboard) => {
            if (!url.startsWith("ws://127.0.0.1")) {
                return;
            }
            if (event.keyCode == cc.macro.KEY.q) {
                this.input_username.string = "123456";
                this.input_password1.string = "123456";
            } else if (event.keyCode == cc.macro.KEY.w) {
                this.input_username.string = "asdfgh";
                this.input_password1.string = "asdfgh";
            }
        }, this);
    }

    checkRemember() {
        let loginInfoStr = localStorage.getItem("userLogin");
        if (loginInfoStr) {
            try {
                let userLogin: UserLogin = JSON.parse(loginInfoStr);
                this.input_username.string = userLogin.username;
                this.input_password1.string = userLogin.username;
                this.isRemember = true;
                this.renderRemember();
            } catch { }
        }
    }

    remember() {
        if (this.isRemember) {
            this.isRemember = false;
        } else {
            this.isRemember = true;
        }
        this.renderRemember();
    }

    renderRemember() {
        if (this.isRemember) {
            this.checkBoxSprite.spriteFrame = this.checkBoxFrames[1];
        } else {
            this.checkBoxSprite.spriteFrame = this.checkBoxFrames[0];
        }
    }

    login() {
        if (this.isLoginPanel) {
            if (this.input_username.string.length < 6) {
                Camper.getInstance().showToast("Tài khoản phải có ít nhất 6 ký tự");
                return;
            }
            if (!JCTool.isLetterOrNum(this.input_username.string)) {
                Camper.getInstance().showToast("Tài khoản chỉ được chứa chữ cái và số");
                return;
            }
            if (this.input_password1.string.length < 6) {
                Camper.getInstance().showToast("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }

            if (JCTool.hasChinese(this.input_password1.string)) {
                Camper.getInstance().showToast("Mật khẩu không được chứa ký tự đặc biệt");
                return;
            }
            let userLogin: UserLogin = {
                username: this.input_username.string,
                password: this.input_password1.string
            };
            GlobalData.player.call("UserController.login", [userLogin.username, userLogin.password], (res) => {
                Camper.getInstance().hideLoading();
                if (res.code == 200) {
                    if (this.isRemember) {
                        localStorage.setItem("userLogin", JSON.stringify(userLogin));
                    } else {
                        localStorage.setItem("userLogin", "");
                    }
                    GlobalData.userInfo = res.data;
                    Camper.getInstance().node.addComponent(ResourceMgr);
                }
                Camper.getInstance().showToast(res.msg);
            });
            Camper.getInstance().showLoading("Đang đăng nhập", true);
        } else {
            this.isLoginPanel = true;
            this.input_password2.node.parent.active = false;
            this.checkBoxSprite.node.parent.active = true;
            this.input_username.string = "";
            this.input_password1.string = "";
            this.input_password2.string = "";
        }
    }

    register() {
        if (this.isLoginPanel) {
            this.isLoginPanel = false;
            this.input_password2.node.parent.active = true;
            this.checkBoxSprite.node.parent.active = false;
            this.input_username.string = "";
            this.input_password1.string = "";
            this.input_password2.string = "";
        } else {
            if (this.input_username.string.length < 6) {
                Camper.getInstance().showToast("Tài khoản phải có ít nhất 6 ký tự");
                return;
            }
            if (!JCTool.isLetterOrNum(this.input_username.string)) {
                Camper.getInstance().showToast("Tài khoản chỉ được chứa chữ cái và số");
                return;
            }
            if (this.input_password1.string.length < 6) {
                Camper.getInstance().showToast("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }

            if (JCTool.hasChinese(this.input_password1.string)) {
                Camper.getInstance().showToast("Mật khẩu không được chứa ký tự đặc biệt");
                return;
            }
            if (this.input_password1.string != this.input_password2.string) {
                Camper.getInstance().showToast("Hai mật khẩu không khớp");
                return;
            }
            let userLogin: UserLogin = {
                username: this.input_username.string,
                password: this.input_password1.string
            };
            GlobalData.player.call("UserController.register", [userLogin.username, userLogin.password], (res) => {
                Camper.getInstance().hideLoading();
                Camper.getInstance().showToast(res.msg);
            });
            Camper.getInstance().showLoading("Đang đăng ký", true);
        }
    }
}
interface UserLogin {
    username: string;
    password: string;
}