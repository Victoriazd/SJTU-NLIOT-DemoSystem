import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { Layout,Menu, Icon,Input,AutoComplete,Button,Card,Col,Row,Form,Select,Typography,Modal,Tree,Radio,Table} from 'antd';
import moment from 'moment';
import 'moment/locale/zh-cn';
import 'antd/dist/antd.css';
import './index.css';

moment.locale('zh-cn');

const { Header, Footer, Sider, Content,} = Layout;

const { SubMenu } = Menu;

const { Text } = Typography;

const { TreeNode } = Tree;


function onSelect(value) {
    console.log('onSelect', value);
}

class App extends React.Component {
    state = {
        dataSource: [],
        loading: true,
        ModalText: "是否发送请求连接该物设备？",
        confirmLoading: false,
        collapsed: false,
        visibleSearch: true,
        visibleList: false,
        visible: false,
        visible2: false,
        confirmLoading2: false,
        dataTable: [],
    };

    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };

    onChange = e => {
        console.log('radio checked', e.target.value);
        this.setState({
            value: e.target.value,
        });
        if(this.state.value==1){
            this.setState({
                display1: 'block',
                display2: 'none',
                display3: 'none',
            })
        }
        else if(this.state.value==2){
            this.setState({
                display1: 'none',
                display2: 'block',
                display3: 'none',
            })
        }
        else{
            this.setState({
                display1: 'none',
                display2: 'none',
                display3: 'block',
            })
        }
    };

    handleSearch = (value) => {
        this.setState({
            dataSource: !value ? [] : [
                value
            ],
        });
    }

    constructor(props){
        super(props);
        this.state={
            list1:[],
            list2:[],
            point:"",
            backpoint: "",
            device:"",
            基本信息:{},
            属性信息:{},
            服务信息:[],
            扩展信息:[],
            ip:'http://192.168.1.103:8080/table',
            test:"",
            display_search:'block',
            display_table:'none',
            display_set:'none',
            value: 1,
            display1:'block',
            display2:'none',
            display3:'none',
            display4:'none'
        }
    }

    handleClick1(){
        if(this.state.display_search == 'none'){
            this.setState({display_search:'block',})
            this.setState({display_table:'none',})
            this.setState({display_set:'none',})
            this.setState({display4:'none',})
        }
    }

    handleClick2=(e)=>{
        if(this.state.display_table == 'none'){
            this.setState({display_search:'none',})
            this.setState({display_set:'none',})
            this.setState({display_table:'block',})
            this.setState({display4:'none',})
        }

        if (e !== "无搜索结果"){
            this.state.backpoint = this.state.point
            axios.get('http://localhost:31020/getItemManagerNodeByName/' + e)
                .then((res)=>{
                    // 注意this指向
                    this.setState({
                        list2:res.data.items,
                        point:e
                    })
                })
                .catch((err)=>{
                    console.log(err)
                })

        }
    }

    handleClick3(){
        if(this.state.display_set == 'none'){
            this.setState({display_search:'none',})
            this.setState({display_table:'none',})
            this.setState({display_set:'block',})
            this.setState({display4:'none',})
        }
    }

    getDataA=(e)=>{
        if (e !== "无搜索结果"){
            this.state.backpoint = this.state.point
            axios.get('http://localhost:31020/getItemManagerNodeByName/' + e)
                .then((res)=>{
                    // 注意this指向
                    this.setState({
                        list1:res.data.itemManagers,
                        list2:res.data.items,
                        point:e
                    })

                })
                .catch((err)=>{
                    console.log(err)
                })
        }
    }

    getFunctionTable=(e)=>{
        this.setState({
            display4:'block',
            display_table:'none',
        });
        axios.get(e)
            .then((res)=>{
                // 注意this指向
                alert(1111111)
                console.log('请求返回的数据');
                console.log(res)//此接口返回数据为jsonp格式，须进一步对数据进行处理

                // this.setState({
                //     基本信息:res.data.基本信息,
                //     属性信息:res.data.属性信息,
                //     服务信息:res.data.服务信息,
                //     扩展信息:res.data.扩展信息,
                //     test:this.state.device,
                // })
            })
            .catch((err)=>{
                alert(err)
                console.log(err)
                this.setState({
                    基本信息:{},
                    属性信息:{},
                    服务信息:[],
                    扩展信息:[],
                    test:"未找到"+this.state.device+"物功能表"
                })
            })
    }

    showModal() {
        // if (e !== "无搜索结果"){
        //     this.setState({
        //         visible: true,
        //         ModalText: "是否发送请求连接该物设备？",
        //     });
        // }
        this.setState({
            visible: true,
            ModalText: "是否发送请求连接该物设备？",
        });
    }

    handleOk = () => {
        this.getFunctionTable(this.state.ip);
        this.setState({
            ModalText:"正在发送请求...",
            confirmLoading: true,
            display4:'block',
            display_table:'none',
        });
        setTimeout(() => {
            this.setState({
                visible: false,
                confirmLoading: false,
                visible2: true,
            });
        }, 2000);
    }

    handleCancel = () => {
        console.log('Clicked cancel button');
        this.setState({
            visible: false,
        });
    }

    handleOk2 = e => {
        console.log(e);
        this.setState({
            visible2: false,
        });
    };

    handleCancel2 = e => {
        console.log(e);
        this.setState({
            visible2: false,
        });
    };

    render() {
        const { dataSource } = this.state;
        const { loading } = this.state;
        const dataTable = [];

        const columns = [
            {
                title: '名称',
                dataIndex: 'name',
                width: '20%',
                render: text => <p><a onClick={() => this.showModal()}>{text}</a></p>,
            },
            {
                title: '注册时间',
                dataIndex: 'createTime',
                width: '20%',
            },
            {
                title: '修改时间',
                dataIndex: 'modifyTime',
                width: '20%',
            },
            {
                title: '类型',
                dataIndex: 'type',
                width: '20%',
            },
            {
                title: '所有人',
                dataIndex: 'user',
                width: '20%',
            },
        ];

        this.state.list2.map((value, key) =>{
            return(
                dataTable.push({
                    key: key,
                    name: value.name,
                    createTime: value.createTime,
                    modifyTime: value.modifyTime,
                    type: value.type,
                    user: value.user,

                })
            )
        });

        const pagination = {
            total: this.state.list2.length,
            showSizeChanger: true,
            onShowSizeChange(current, pageSize) {
                console.log('Current: ', current, '; PageSize: ', pageSize)
            },
            onChange(current) {
                console.log('Current: ', current)
            }
        }

        return (
            <div>
                <Layout>
                    <Header className="logo">
                        <div>
                            <h1 className="title ">物浏览器</h1>
                        </div>
                    </Header>
                    <Layout style={{marginTop: 2}}>
                        <Sider className="sidebar">
                            <div style={{ width: 60, marginTop: 2}}>
                                <Menu
                                    className={"menu1"}
                                    defaultSelectedKeys={['1']}
                                    defaultOpenKeys={['sub1']}
                                    mode="inline"
                                    theme="dark"
                                    inlineCollapsed={this.state.collapsed}>

                                    <Menu.Item key="1"
                                               onClick={this.handleClick1.bind(this)}
                                               className="menusize"
                                    >
                                        <Icon type="global" />
                                        <span>物搜索服务</span>
                                    </Menu.Item>

                                    <SubMenu
                                        key="sub1"
                                        title={
                                            <span>
                                                <Icon type="code-sandbox" />
                                                <span>本地物关</span>
                                            </span>
                                        }
                                    >
                                        <SubMenu
                                            key="sub1-2"
                                            title={
                                                <span>
                                                <Icon type="code-sandbox" />
                                                <span>物列表</span>
                                            </span>
                                            }
                                            onTitleClick={() => this.handleClick2(this.state.dataSource)}
                                        >
                                            {
                                                this.state.list2.map((value,key)=>{
                                                    return (
                                                        <Menu.Item
                                                            key={key}
                                                        >
                                                            <Icon type="folder"/>
                                                            <span>{value.name}</span>
                                                        </Menu.Item>
                                                    )
                                                })
                                            }
                                        </SubMenu>

                                        <Menu.Item key="6"
                                                   onClick={this.handleClick3.bind(this)}
                                        >
                                            <Icon type="setting" />
                                            <span>物关设置</span>
                                        </Menu.Item>
                                    </SubMenu>

                                </Menu>
                            </div>
                        </Sider>
                        <Content>
                            <div className="search" style={{display: this.state.display_search}}>
                                <Radio.Group
                                    onChange={this.onChange}
                                    value={this.state.value}
                                    defaultValue={this.state.value}
                                    style={{marginBottom:5}}
                                >
                                    <Radio value={1} style={{marginLeft:5}}>物关搜索</Radio>
                                    <Radio value={2} style={{marginLeft:20}}>物设备搜索</Radio>
                                    <Radio value={3} style={{marginLeft:20}}>附近物搜索</Radio>
                                </Radio.Group>
                                <AutoComplete
                                    className="global-search"
                                    size="large"
                                    style={{ width: '100%', display: this.state.display1 }}
                                    dataSource={dataSource}
                                    onSelect={onSelect}
                                    onSearch={this.handleSearch}
                                    placeholder="请输入物关名称"
                                    optionLabelProp="text"
                                >
                                    <Input onPressEnter={() => this.getDataA(this.state.dataSource)}
                                           suffix={(
                                               <Button className="search-btn" size="large" type="primary" onClick={() => this.getDataA(this.state.dataSource)}>
                                                   <Icon type="search" />
                                               </Button>
                                           )}
                                    />
                                </AutoComplete>
                                <AutoComplete
                                    className="global-search"
                                    size="large"
                                    style={{ width: '100%', display: this.state.display2 }}
                                    dataSource={dataSource}
                                    onSelect={onSelect}
                                    onSearch={this.handleSearch}
                                    placeholder="请输入物设备名称"
                                    optionLabelProp="text"
                                >
                                    <Input onPressEnter={() => this.getDataA(this.state.dataSource)}
                                           suffix={(
                                               <Button className="search-btn" size="large" type="primary" onClick={() => this.getDataA(this.state.dataSource)}>
                                                   <Icon type="search" />
                                               </Button>
                                           )}
                                    />
                                </AutoComplete>
                                <AutoComplete
                                    className="global-search"
                                    size="large"
                                    style={{ width: '100%', display: this.state.display3 }}
                                    dataSource={dataSource}
                                    onSelect={onSelect}
                                    onSearch={this.handleSearch}
                                    placeholder="请输入物类型"
                                    optionLabelProp="text"
                                >
                                    <Input onPressEnter={() => this.getDataA(this.state.dataSource)}
                                           suffix={(
                                               <Button className="search-btn" size="large" type="primary" onClick={() => this.getDataA(this.state.dataSource)}>
                                                   <Icon type="search" />
                                               </Button>
                                           )}
                                    />
                                </AutoComplete>
                            </div>
                            <div className="table" style={{background: '#ECECEC', padding: '30px', display: this.state.display_table}}>

                                <Table columns={columns} dataSource={dataTable} scroll={{ y: 10000 }} bordered pagination={pagination} />

                                <div>
                                    <Modal
                                        title="请求连接"
                                        visible={this.state.visible}
                                        onOk={this.handleOk}
                                        confirmLoading={this.state.confirmLoading}
                                        onCancel={this.handleCancel}
                                    >
                                        <p>{this.state.ModalText}</p>
                                    </Modal>
                                </div>
                            </div>

                            <div className="set" style={{display: this.state.display_set}}>
                                <div>
                                    <Button type="primary" style={{marginTop:100, marginLeft:400}}>物关名称：</Button>
                                    {/* <Text className="text" code>物关名称：</Text> */}
                                    <Input placeholder="省/市/县（区）/乡镇/街道(村)/门牌号/详细地址" style={{width:600}} />
                                </div>
                                <div>
                                    <Button type="primary" style={{marginTop:100, marginLeft:750}}>设置</Button>
                                </div>
                            </div>

                            <div
                                style={{display: this.state.display4}}
                            >
                                <p>{this.state.test}</p>
                                <Tree
                                    onSelect={this.onSelect}
                                    onCheck={this.onCheck}
                                >
                                    <TreeNode title="基本信息" >
                                        <TreeNode title={<p>物名:{this.state.基本信息.物名}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>物类型:{this.state.基本信息.物类型}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>物号:{this.state.基本信息.物号}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>物通信地址:{this.state.基本信息.物通信地址}</p>} >
                                        </TreeNode>

                                    </TreeNode>
                                    <TreeNode title="属性信息" >
                                        <TreeNode title={<p>开发商:{this.state.属性信息.物生产商}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>用户名:{this.state.属性信息.物用户名}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>重量:{this.state.属性信息.重量}</p>} >
                                        </TreeNode>
                                        <TreeNode title={<p>颜色:{this.state.属性信息.颜色}</p>} >
                                        </TreeNode>
                                    </TreeNode>
                                    <TreeNode title="服务信息" >
                                        {
                                            this.state.服务信息.map((value,key)=>{
                                                return (
                                                    <TreeNode title={<p>服务名称：{value.服务名称}</p>} >
                                                        <TreeNode title={<p>数据类型：{value.数据类型}</p>} >
                                                        </TreeNode>
                                                        <TreeNode title={<a href={this.state.基本信息.物通信地址+value.接口地址}>服务地址: {this.state.基本信息.物通信地址}{value.接口地址}</a>} >
                                                        </TreeNode>
                                                        {/*<TreeNode title={<p>协议参数：{value.协议参数}</p>} >*/}
                                                        {/*{*/}
                                                        {/*() => this.协议参数填写(value)*/}
                                                        {/*}*/}
                                                        {/*{*/}
                                                        {/*() => this.参数描述填写(value)*/}
                                                        {/*}*/}

                                                        {/*</TreeNode>*/}
                                                        <TreeNode title={<p>备注：{value.备注}</p>} >
                                                        </TreeNode>
                                                    </TreeNode>
                                                )
                                            })
                                        }
                                    </TreeNode>
                                    <TreeNode title="扩展信息" >
                                        {
                                            this.state.扩展信息.map((value,key)=>{
                                                return (
                                                    <TreeNode title={<p>联系方式：{value.联系方式}</p>} >
                                                        <TreeNode title={<p>联系地址：{value.联系地址}</p>} >
                                                        </TreeNode>
                                                    </TreeNode>
                                                )
                                            })
                                        }
                                    </TreeNode>
                                    {/*hard code*/}
                                </Tree>
                            </div>
                        </Content>
                    </Layout>
                </Layout>
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));
