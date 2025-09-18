export const  subjectData = [
  { contentType: 'text', label: '文本', content: '开放签', checked: false, edit: false,defaultContent:'开放签',contentOrder:1 },
  { contentType: 'business_line_name', label: '业务线名称', content: '', checked: true, edit: false, defaultContent:'劳动合同-2023年新版' ,contentOrder:2 },
  {
    contentType: 'datetime', label: '日期', content: 'yyyy', defaultContent:'年月日,示例：20231218', edit: false,contentOrder:3, options: [
      { label: '年,示例：2023', value: 'yyyy' },
      { label: '年月,示例：202312', value: 'yyyyMM' },
      { label: '年月日,示例：20231214', value: 'yyyyMMdd' },
      { label: '年月日时，示例：2023121417', value: 'yyyyMMddHH' },
      { label: '年月日时分，示例：202312141747', value: 'yyyyMMddHHmm' },
      { label: '年月日时分秒，示例：20231214174723', value: 'yyyyMMddHHmmss' },
    ], checked: true
  },
  {
    contentType: 'serialnum', label: '流水号', content: '6', checked: true, edit: false, defaultContent:'6位，示例：000001',contentOrder:4, options: [
      { label: '1位，示例：0', value: '1' },
      { label: '2位，示例：01', value: '2' },
      { label: '3位，示例：001', value: '3' },
      { label: '4位，示例：0001', value: '4' },
      { label: '5位，示例：00001', value: '5' },
      { label: '6位，示例：000001', value: '6' },
    ]
  },
  { contentType: 'timestamp', label: '时间戳', content: '', checked: false, edit: false, defaultContent:'示例：1633053958', contentOrder:5, },
  { contentType: 'sender_name', label: '发起人姓名', content: '', checked: false, edit: false, defaultContent:'' ,contentOrder:6,},
  { contentType: 'receiver_name', label: '接收方姓名', content: '', checked: false, edit: false, defaultContent:'' ,contentOrder:7 },
]

export const codeData = [
  { contentType: 'text', label: '文本', content: 'KFQ', checked: true, edit: false,defaultContent:'KFQ',contentOrder:1  },
  {
    contentType: 'datetime', label: '日期', content: 'yyyy', edit: false, defaultContent:'年月日,示例：20231218',contentOrder:2, options: [
      { label: '年,示例：2023', value: 'yyyy' },
      { label: '年月,示例：202312', value: 'yyyyMM' },
      { label: '年月日,示例：20231214', value: 'yyyyMMdd' },
      { label: '年月日时，示例：2023121417', value: 'yyyyMMddHH' },
      { label: '年月日时分，示例：202312141747', value: 'yyyyMMddHHmm' },
      { label: '年月日时分秒，示例：20231214174723', value: 'yyyyMMddHHmmss' },
    ], checked: true
  },
  {
    contentType: 'serialnum', label: '流水号', content: '6', defaultContent:'6位，示例：000001' ,checked: true, contentOrder:3,edit: false, options: [
      { label: '1位，示例：0', value: '1' },
      { label: '2位，示例：01', value: '2' },
      { label: '3位，示例：001', value: '3' },
      { label: '4位，示例：0001', value: '4' },
      { label: '5位，示例：00001', value: '5' },
      { label: '6位，示例：000001', value: '6' },
    ]
  },
  { contentType: 'timestamp', label: '时间戳', content: '',defaultContent:'示例：1633053958', checked: false, edit: false ,contentOrder:4},
]
