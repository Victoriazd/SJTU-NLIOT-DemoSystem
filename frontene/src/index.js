import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { Layout,Menu, Icon,Input,AutoComplete,Button,Card,Col,Row,Form,Select,Typography,Modal} from 'antd';
import moment from 'moment';
import 'moment/locale/zh-cn';
import 'antd/dist/antd.css';
import './index.css';

moment.locale('zh-cn');

const { Header, Footer, Sider, Content,} = Layout;

const { SubMenu } = Menu;

const { Text } = Typography;

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
    };

    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
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
            设备属性:{},
            服务信息:[],
            扩展信息:[],
            ip:"http://192.168.1.111:8085",
            test:"",
            display_search:'block',
            display_table:'none',
            display_set:'none',
        }
    }

    handleClick1(){
        if(this.state.display_search == 'none'){
            this.setState({display_search:'block',})
            this.setState({display_table:'none',})
            this.setState({display_set:'none',})
        }
    }

    handleClick2=(e)=>{
        if(this.state.display_table == 'none'){
            this.setState({display_search:'none',})
            this.setState({display_set:'none',})
            this.setState({display_table:'block',})
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
                    alert(this.state.list2.toString())
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

    showModal = (e) => {
        if (e !== "无搜索结果"){
            this.setState({
                visible: true,
                ModalText: "是否发送请求连接该物设备？",
                device: e
            });
        }

    }

    handleOk = () => {
        this.getFunctionTable();
        this.setState({
            ModalText:"正在发送请求...",
            confirmLoading: true,
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

    render() {
        const { dataSource } = this.state;
        const { loading } = this.state;
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
                                        <Menu.Item
                                            key="5"
                                            onClick={() => this.handleClick2(this.state.dataSource)}
                                        >
                                            <Icon type="unordered-list" />
                                            <span>物列表</span>
                                        </Menu.Item>
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
                                <AutoComplete
                                    className="global-search"
                                    size="large"
                                    style={{ width: '100%' }}
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
                            </div>
                            <div className="table" style={{background: '#ECECEC', padding: '30px', display: this.state.display_table}}>

                                <Card
                                    title="物设备"
                                    loading={loading}
                                >
                                    <div>
                                        {
                                            this.state.list2.map((value,key)=>{
                                                return (
                                                    <Row gutter={16}>
                                                        <Col span={8}>
                                                            <Card title={value.name} bordered={false}>
                                                                <p key={key}><span className="card">物功能表：</span><a onClick={() => this.showModal(value.url)}>{value.url}</a></p>
                                                            </Card>
                                                        </Col>
                                                    </Row>
                                                )
                                            })
                                        }
                                    </div>
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
                                </Card>
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
                        </Content>
                    </Layout>
                </Layout>
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));
