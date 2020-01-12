import React from 'react';
import './App.css';
import { DatePicker } from 'antd';

function App() {
    var handle=()=> {
        const w = window.open('https://www.baidu.com/');
    }
  return (
    <div>
      <header className="App-header">
        <h1>调试工具</h1>
        <p> </p>
        <button className="button" onClick={handle}>物搜索</button>
        <p> </p>
        <button className="button" onClick={handle}>物连接</button>
      </header>
    </div>

  );

}

export default App;

//agent or thing or item
//物关
