module.exports = {
  name: 'resrun-paas-web',
  entry: 'http://localhost:8901',
  routes: [
    {
      name: 'sub-app-b',
      entry: '//localhost:3002',
      activeRule: '/sub-app-b'
    }
  ]
}
