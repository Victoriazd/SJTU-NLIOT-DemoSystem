import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import fetchJsonp from 'fetch-jsonp';
import {message, Alert, Icon, Layout, Menu, Divider, Typography, AutoComplete, Input,Button , Card, Modal, Tree, Descriptions, Table, Select, Collapse} from 'antd';
import 'moment/locale/zh-cn';
import "antd/dist/antd.css";
import "./index.css";

const { Title } = Typography;
const { Column, ColumnGroup } = Table;
const { Option } = Select;
const Panel = Collapse.Panel;
const {
    Header, Footer, Sider, Content,
} = Layout;
const { Text } = Typography;
const { TreeNode } = Tree;

const data1 = {
    基本信息:{
        设备名称:"显微镜1",
        设备类型:"显微镜",
        设备id:"0000000000000000",
        设备地址:"127.0.0.1:2233"
    },
    设备属性:{
        开发商:"舜宇",
        所有人:"上海交大软件学院"
    },
    服务信息:[
        {
            服务名称:"实时图像",
            数据类型:"视频流",
            接口地址:"/video.mp4",
            协议类型:"",
            协议参数:{
                "":""
            },
            备注:""
        },
        {
            服务名称:"瞬间图像",
            数据类型:"块",
            接口地址:"/image.png",
            协议类型:"",
            协议参数:{
                "":""
            },
            备注:""
        }
    ],
    扩展信息:[]
};

const data = [
    {
        key: '1',
        接口名称: "实时图像",
        数据类型: "视频流",
        接口地址: "/video.mp4/",
        接口参数: [],
        参数描述:"无参数"
    },
    {
        key: '2',
        接口名称: "瞬间图像",
        数据类型: "块",
        接口地址: "/image.png/",
        接口参数:[
            "图像1",
            "图像2",
            "图像3"
        ],
        参数描述:"参数为时间，格式为YYYY-MM-DD-hh-mm"
    },
];


function onSelect(value) {
    console.log('onSelect', value);
}

class App extends React.Component {
    state = {
        dataSource: [],
        loading: true,
        ModalText: "是否发送请求连接该物设备？",
        visible: false,
        confirmLoading: false,
        visible2: false,
        confirmLoading2: false,
    }



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
            test:""

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

    getFunctionTableURL=()=>{
        axios.get('http://localhost:31020/getFunctionTableByTool/' + this.state.point + "/" + this.state.device)
            .then((res)=>{
                // 注意this指向
                this.setState({
                    ip:res.data
                })
            })
            .catch((err)=>{
                console.log(err)
            })
    }

    getFunctionTable=()=>{

        axios.get(this.state.ip+'/%E7%89%A9%E5%8A%9F%E8%83%BD%E8%A1%A8')
            .then((res)=>{
                // 注意this指向
                this.setState({
                    基本信息:res.data.基本信息,
                    设备属性:res.data.设备属性,
                    服务信息:res.data.服务信息,
                    扩展信息:res.data.扩展信息,
                    test:this.state.device,
                })
            })
            .catch((err)=>{
                console.log(err)
                this.setState({
                    基本信息:{},
                    设备属性:{},
                    服务信息:[],
                    扩展信息:[],
                    test:"未找到"+this.state.device+"物功能表"
                })
            })
    }

    getDataF = (e)=>{
        if (e !== "无搜索结果"){
            let list1 = []
            axios.get('http://localhost:31020/getItemManagerNodeByName/' + e)
                .then((res)=>{
                    list1 = res.data.itemManagers
                })
                .catch((err)=>{
                    console.log(err)
                })
            return( <TreeNode title={<a onClick={() => this.getDataA(e)}>{e}</a>} >
                {list1.map((value)=>{
                    return (
                        <div>
                            {() => this.getDataF(value.name)}
                        </div>
                    )
                })}
            </TreeNode>);

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

    onSelect1 = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    onCheck = (checkedKeys, info) => {
        console.log('onCheck', checkedKeys, info);
    };

    协议参数填写 = (e) =>{
        if(e.协议参数.参数 != null)
            return(<TreeNode title={<p>参数：{e.协议参数.参数}</p>} >
                </TreeNode>
    )
    };

    参数描述填写 = (e) =>{
        if(e.协议参数.描述 != null)
            return(<TreeNode title={<p>描述：{e.协议参数.描述}</p>} >
                </TreeNode>
            )
    };


    render() {
        const { dataSource } = this.state;
        const { loading } = this.state;
        return (
            <div>
                <Layout className={"layout"}>
                    <Header >
                        <div>
                            <h1 className="title ">交大物联调试工具</h1>
                        </div>
                    </Header>
                    <Layout>
                        <Sider width={350} theme={"light"} className={"pad"}>
                            <p>起始物节点：上海交通大学闵行校区</p>
                            <Tree
                                onSelect={this.onSelect}
                                onCheck={this.onCheck}
                            >
                                <TreeNode title={<a onClick={() => this.getDataA("上海交通大学闵行校区")}>上海交通大学闵行校区</a>} >
                                    <TreeNode title={<a onClick={() => this.getDataA("上海交通大学软件学院")}>上海交通大学软件学院</a>} >
                                        <TreeNode title={<a onClick={() => this.getDataA("上海交通大学闵行校区软件大楼3501")}>上海交通大学闵行校区软件大楼3501</a>} >

                                        </TreeNode>
                                        <TreeNode title={<a onClick={() => this.getDataA("上海交通大学闵行校区软件大楼3503")}>上海交通大学闵行校区软件大楼3503</a>} >

                                        </TreeNode>
                                    </TreeNode>
                                    <TreeNode title={<a onClick={() => this.getDataA("上海交通大学工程训练中心")}>上海交通大学工程训练中心</a>} >
                                    </TreeNode>
                                </TreeNode>
                                {/*hard code*/}
                            </Tree>
                            {/*<Tree*/}
                                {/*onSelect={this.onSelect}*/}
                                {/*onCheck={this.onCheck}*/}
                            {/*>*/}
                                {/*{this.getDataF("上海交通大学闵行校区")}*/}
                            {/*</Tree>*/}
                        </Sider>
                        <Content className={"pad"}>
                            <div className="global-search-wrapper">
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

                            <h2>搜索结果</h2>

                            <div>
                                <p>当前物关：{this.state.point}</p>
                                <Card
                                    title="下级物关"
                                    loading={loading}
                                >
                                    <div>
                                        {
                                            this.state.list1.map((value,key)=>{
                                                return (
                                                    <div>
                                                        <p key={key}><a onClick = {() => this.getDataA(value.name)}>{value.name}</a></p>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </Card>
                                <Divider />
                                <Card
                                    title="物设备"
                                    loading={loading}
                                >
                                    <div>
                                        {
                                            this.state.list2.map((value,key)=>{
                                                return (
                                                    <div>
                                                        <p key={key}><a onClick={() => this.showModal(value.name)}>{value.name}</a></p>
                                                    </div>
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
                                    <div>
                                        {/*<Modal*/}
                                            {/*title="物功能表"*/}
                                            {/*visible={this.state.visible2}*/}
                                            {/*onOk={this.handleOk2}*/}
                                            {/*onCancel={this.handleCancel2}*/}
                                            {/*width={1500}*/}
                                        {/*>*/}
                                           {/*<div>*/}
                                               {/*<Descriptions title={this.state.device} bordered>*/}
                                                   {/*<Descriptions.Item label="设备名称">显微镜1</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="设备类型">显微镜</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="设备地址">127.0.0.1：2233</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="所有人">上海交通大学软件学院</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="开发商">舜宇</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="物设备其他信息">*/}
                                                       {/*型号：xxxxx*/}
                                                       {/*<br />*/}
                                                       {/*颜色：蓝白*/}
                                                       {/*<br />*/}
                                                       {/*出厂时间：yyyy.mm.dd*/}
                                                       {/*<br />*/}
                                                   {/*</Descriptions.Item>*/}
                                                   {/*<Descriptions.Item label="服务接口">*/}
                                                       {/*<Table dataSource={data}>*/}
                                                           {/*<Column title="接口名称" dataIndex="接口名称" key="接口名称" />*/}
                                                           {/*<Column title="数据类型" dataIndex="数据类型" key="数据类型" />*/}
                                                           {/*<Column*/}
                                                               {/*title="接口地址"*/}
                                                               {/*key="接口地址"*/}
                                                               {/*render={(record) => (*/}
                                                                   {/*<span>*/}
                                                                       {/*<a href={"http://" + this.state.ip + record.接口地址}>{"http://" + this.state.ip + record.接口地址}</a>*/}
                                                                   {/*</span>*/}
                                                               {/*)}*/}
                                                           {/*/>*/}
                                                           {/*<Column*/}
                                                               {/*title="接口参数"*/}
                                                               {/*key="接口参数"*/}
                                                               {/*render={(record) => (*/}
                                                                   {/*<span>*/}
                                                                       {/*<Input placeholder="请输入服务接口参数" />*/}
                                                                   {/*</span>*/}
                                                               {/*)}*/}
                                                           {/*/>*/}
                                                           {/*<Column title="参数描述" dataIndex="参数描述" key="参数描述" />*/}
                                                       {/*</Table>*/}
                                                   {/*</Descriptions.Item>*/}

                                               {/*</Descriptions>*/}
                                           {/*</div>*/}
                                        {/*</Modal>*/}


                                        {/*<Modal*/}
                                            {/*title="物功能表"*/}
                                            {/*visible={this.state.visible2}*/}
                                            {/*onOk={this.handleOk2}*/}
                                            {/*onCancel={this.handleCancel2}*/}
                                            {/*width={1200}*/}
                                        {/*>*/}
                                            {/*<Collapse bordered={false}>*/}
                                                {/*<Panel header="基本信息" key="1">*/}
                                                    {/*设备名称:显微镜1*/}
                                                    {/*<br/>*/}
                                                    {/*设备类型:显微镜*/}
                                                    {/*<br/>*/}
                                                    {/*设备id  :0000000000000000*/}
                                                    {/*<br/>*/}
                                                    {/*设备地址:127.0.0.1:2233*/}
                                                    {/*<br/>*/}
                                                {/*</Panel>*/}
                                                {/*<Panel header="设备属性" key="2">*/}
                                                    {/*<Collapse bordered={false}>*/}
                                                        {/*<Panel header="开发商：舜宇"/>*/}
                                                        {/*<Panel header="所有人">*/}
                                                            {/*上海交通大学软件学院*/}
                                                        {/*</Panel>*/}
                                                    {/*</Collapse>*/}
                                                {/*</Panel>*/}
                                                {/*<Panel header="服务信息" key="3">*/}
                                                    {/*<Table dataSource={data}>*/}
                                                        {/*<Column title="接口名称" dataIndex="接口名称" key="接口名称" />*/}
                                                        {/*<Column title="数据类型" dataIndex="数据类型" key="数据类型" />*/}
                                                        {/*<Column*/}
                                                            {/*title="接口地址"*/}
                                                            {/*key="接口地址"*/}
                                                            {/*render={(record) => (*/}
                                                                {/*<span>*/}
                                                                       {/*<a href={"http://" + this.state.ip + record.接口地址}>{"http://" + this.state.ip + record.接口地址}</a>*/}
                                                                   {/*</span>*/}
                                                            {/*)}*/}
                                                        {/*/>*/}
                                                        {/*<Column*/}
                                                            {/*title="接口参数"*/}
                                                            {/*key="接口参数"*/}
                                                            {/*render={(record) => (*/}
                                                                {/*<span>*/}
                                                                       {/*<Input placeholder="请输入服务接口参数" />*/}
                                                                   {/*</span>*/}
                                                            {/*)}*/}
                                                        {/*/>*/}
                                                        {/*<Column title="参数描述" dataIndex="参数描述" key="参数描述" />*/}
                                                    {/*</Table>*/}
                                                {/*</Panel>*/}
                                                {/*<Panel header="拓展信息" key="4">*/}
                                                    {/*4*/}
                                                {/*</Panel>*/}
                                            {/*</Collapse>*/}
                                        {/*</Modal>*/}

                                        <Modal
                                            title="物功能表"
                                            visible={this.state.visible2}
                                            onOk={this.handleOk2}
                                            onCancel={this.handleCancel2}
                                            width={1200}
                                        >
                                            <p>{this.state.test}</p>
                                            <Tree
                                                onSelect={this.onSelect}
                                                onCheck={this.onCheck}
                                            >
                                                <TreeNode title="基本信息" >
                                                    <TreeNode title={<p>设备名称:{this.state.基本信息.设备名称}</p>} >
                                                    </TreeNode>
                                                    <TreeNode title={<p>设备类型:{this.state.基本信息.设备类型}</p>} >
                                                    </TreeNode>
                                                    <TreeNode title={<p>设备id:{this.state.基本信息.设备id}</p>} >
                                                    </TreeNode>
                                                    <TreeNode title={<p>设备地址:{this.state.基本信息.设备地址}</p>} >
                                                    </TreeNode>

                                                </TreeNode>
                                                <TreeNode title="设备属性" >
                                                    <TreeNode title={<p>开发商:{this.state.设备属性.开发商}</p>} >
                                                    </TreeNode>
                                                    <TreeNode title={<p>所有人:{this.state.设备属性.所有人}</p>} >
                                                    </TreeNode>
                                                </TreeNode>
                                                <TreeNode title="服务信息" >
                                                    {
                                                        this.state.服务信息.map((value,key)=>{
                                                            return (
                                                                <TreeNode title={<p>服务名称：{value.服务名称}</p>} >
                                                                    <TreeNode title={<p>数据类型：{value.数据类型}</p>} >
                                                                    </TreeNode>
                                                                    <TreeNode title={<a href={this.state.ip+value.接口地址}>服务地址: {this.state.ip}{value.接口地址}</a>} >
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
                                                                <TreeNode title={<p>公告：{value.公告}</p>} >
                                                                </TreeNode>
                                                            )
                                                        })
                                                    }
                                                </TreeNode>
                                                {/*hard code*/}
                                            </Tree>
                                        </Modal>
                                    </div>


                                </Card>
                            </div>
                            <Divider />
                            <div><Button type="primary" icon="undo" onClick={() => this.getDataA(this.state.backpoint)}>返回</Button></div>
                        </Content>
                    </Layout>
                </Layout>

            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));




