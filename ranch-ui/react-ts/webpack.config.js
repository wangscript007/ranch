const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsWebpackPlugin = require('uglifyjs-webpack-plugin');
const ExtractTextWebpackPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

var htmls = [{
    name: 'index',
    path: 'mobile/index'
}, {
    name: 'classify',
    path: 'mobile/classify/index'
}, {
    name: 'doc',
    path: 'mobile/doc/index'
}, {
    name: 'doc-read',
    path: 'mobile/doc/read'
}, {
    name: 'order',
    path: 'mobile/order/index'
}, {
    name: 'mine',
    path: 'mobile/mine/index'
}, {
    name: 'sign-modify',
    path: 'mobile/sign/modify'
}, {
    name: 'sign-password',
    path: 'mobile/sign/password'
}, {
    name: 'mobile-sign-in',
    path: 'mobile/sign/index'
}, {
    name: 'console',
    path: 'console/index',
    template: 'console'
}, {
    name: 'console-sign-in',
    path: 'console/sign-in'
}];

var entry = {};
var plugins = [new CleanWebpackPlugin(['dist'])];
for (var i = 0; i < htmls.length; i++) {
    entry[htmls[i].name] = './src/' + htmls[i].path + '.tsx';
    plugins.push(new HtmlWebpackPlugin({
        filename: htmls[i].name + '.html',
        template: './src/template/' + (htmls[i].template || 'index') + '.html',
        chunks: [htmls[i].name],
        minify: {
            collapseWhitespace: true
        },
        title: 'Ranch UI'
    }));
}
plugins.push(new UglifyJsWebpackPlugin());
plugins.push(new ExtractTextWebpackPlugin({
    filename: (getPath) => {
        return getPath('css/[contenthash:8].css');
    }
}));
plugins.push(new CopyWebpackPlugin([{
    context: './src/template/',
    from: '**'
}, {
    from: './node_modules/react/umd/react.production.min.js',
    to: 'js/react/min.js'
}, {
    from: './node_modules/react-dom/umd/react-dom.production.min.js',
    to: 'js/react/dom.min.js'
}]));

module.exports = {
    entry: entry,

    output: {
        filename: 'js/[chunkhash:8].js',
        path: __dirname + '/dist'
    },

    plugins: plugins,

    resolve: {
        extensions: ['.ts', '.tsx', '.js', '.json']
    },

    module: {
        rules: [{
                test: /\.tsx?$/,
                loader: 'awesome-typescript-loader',
                options: {
                    minimize: true
                }
            },
            {
                test: /\.(less)$/,
                use: ExtractTextWebpackPlugin.extract({
                    fallback: 'style-loader',
                    use: [{
                        loader: 'css-loader',
                        options: {
                            minimize: true
                        }
                    }, 'less-loader']
                })
            },
            {
                test: /\.(css)$/,
                use: ExtractTextWebpackPlugin.extract({
                    fallback: 'style-loader',
                    use: {
                        loader: 'css-loader',
                        options: {
                            minimize: true
                        }
                    }
                })
            },
            {
                test: /\.(png)|(jpg)|(jpeg)|(svg)$/,
                loader: 'file-loader',
                options: {
                    publicPath: '../',
                    outputPath: 'img/',
                    name: '[hash:8].[ext]'
                }
            }
        ]
    },

    externals: {
        "react": "React",
        "react-dom": "ReactDOM"
    }
};