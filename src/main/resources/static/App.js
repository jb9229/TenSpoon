// using an es6 transpiler, like babel
//import { Router, Route, Link } from 'react-router/umd/ReactRouter'

// using globals
var Router          =   window.ReactRouter.Router;
var Link            =   window.ReactRouter.Link;
var Route           =   window.ReactRouter.Route;
var IndexRoute      =   window.ReactRouter.IndexRoute;
var browserHistory  =   window.ReactRouter.browserHistory;

class Home extends React.Component {
    render() {
        return (
            <h2>Hey, I am HOME!</h2>
        );
    }
}

class Login extends React.Component {
    render() {
        return (
            <h2>Hey, I am Login!</h2>
        );
    }
}

class AuthEmail extends React.Component {
    authAccountMail() {
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            cache: false,
            statusCode: {
                200: function() {
                    alert( "성공 하였습니다." );
                },
                400: function() {
                    alert( "실패 하였습니다." );
                }
            }.bind(this),
            error: function(xhr, status, err){
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    }

    contextTypes() {
        router: React.PropTypes.func;
    }

    componentDidMount() {
        this.authAccountMail();
    }

    render() {
        var { email } = this.context.router.getCurrentQuery()

        return (
            <div className="accountBox">
                <p>가입을 위한 사용자 이메일 인증에 xx 하였습니다.</p>
                <AuthEmail data={this.state.data} />
            </div>
        );
    }
}


class App extends React.Component {
    render() {

        return (
            <div>
                <ul>
                    <li><Link to="home">Home</Link></li>
                    <li><Link to="login">Longin</Link></li>
                    <li><Link to="/api/v1/accounts/auth?email=abc@d.com">AuthMail</Link></li>
                </ul>
                {this.props.children}
            </div>
        );
    }
}



ReactDOM.render(
    <Router history = {browserHistory}>
        <Route  path = "/"   component ={App}>
            <IndexRoute component = {Home} />
            <Route path="home" component={Home} />
            <Route path="login" component={Login} />
            <Route path="/api/v1/accounts/auth" component={AuthEmail} />
        </Route>
    </Router>
    ,document.body);
